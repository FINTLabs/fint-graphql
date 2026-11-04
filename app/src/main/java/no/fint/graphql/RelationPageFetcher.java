package no.fint.graphql;

import com.google.common.hash.Hashing;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.Link;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Loads large relation lists by walking the target collection's pages instead of requesting
 * every resource on its own. Found resources are placed in the query cache under the relation's
 * own link, so the resolver's ordinary per-link lookup picks them up. Anything a walk does not
 * find, for any reason, is simply requested on its own as before.
 *
 * <p>Entries are matched on their self links rather than on systemId. FINT generates one self
 * link per populated identifier, so this works for relations using any identifier and needs no
 * knowledge of the resource type. Only wanted entries are converted to resources, and pages are
 * not cached, so a walk over a whole collection holds at most one page in memory.
 */
@Slf4j
@Component
public class RelationPageFetcher {

    private final WebClientRequest webClientRequest;
    private final ObjectMapper objectMapper;
    private final int threshold;
    private final int pageSize;

    public RelationPageFetcher(
            WebClientRequest webClientRequest,
            ObjectMapper objectMapper,
            @Value("${fint.graphql.relation-pagination.threshold:50}") int threshold,
            @Value("${fint.graphql.relation-pagination.page-size:500}") int pageSize) {
        if (threshold < 0) {
            throw new IllegalArgumentException("fint.graphql.relation-pagination.threshold must not be negative");
        }
        // The client API answers size=0 with the whole collection, so it can never mean "a page".
        if (pageSize <= 0) {
            throw new IllegalArgumentException("fint.graphql.relation-pagination.page-size must be positive");
        }
        this.webClientRequest = webClientRequest;
        this.objectMapper = objectMapper;
        this.threshold = threshold;
        this.pageSize = pageSize;
    }

    /** Completes when the query cache holds what the pages could provide. Never fails on a page error. */
    public <T> Mono<Void> prefetch(List<Link> links, Class<T> type, DataFetchingEnvironment dfe) {
        if (links.size() <= threshold) {
            return Mono.empty();
        }
        return Flux.fromIterable(groupByCollection(links).entrySet())
                .filter(group -> group.getValue().size() > threshold)
                .flatMap(group -> webClientRequest.oncePerQuery(
                        walkKey(group.getKey(), group.getValue()), type,
                        () -> walk(group.getKey(), group.getValue(), type, dfe), dfe))
                .then();
    }

    /** Resource id to the relation hrefs naming it, per collection. Links without an id stay individual. */
    private Map<String, Map<String, Set<String>>> groupByCollection(List<Link> links) {
        Map<String, Map<String, Set<String>>> collections = new LinkedHashMap<>();
        for (Link link : links) {
            parse(link.getHref()).ifPresent(id -> collections
                    .computeIfAbsent(id.collection(), ignored -> new HashMap<>())
                    .computeIfAbsent(id.id(), ignored -> new LinkedHashSet<>())
                    .add(link.getHref()));
        }
        return collections;
    }

    /** Identical relations in one query, such as a parent reached by several paths, share a walk. */
    private String walkKey(String collection, Map<String, Set<String>> wanted) {
        String ids = new TreeSet<>(wanted.keySet()).toString();
        return "relation-pages:" + collection + ":" + Hashing.murmur3_128().hashUnencodedChars(ids);
    }

    private <T> Mono<Void> walk(String collection, Map<String, Set<String>> wanted, Class<T> type,
                                DataFetchingEnvironment dfe) {
        Map<String, Set<String>> remaining = new HashMap<>(wanted);
        AtomicInteger pages = new AtomicInteger();
        String firstPage = collection + "?size=" + pageSize;
        Set<String> visited = new HashSet<>(Set.of(firstPage));
        return fetchPage(firstPage, remaining, pages, type, dfe)
                .expand(page -> {
                    String next = remaining.isEmpty() ? null : nextPage(page);
                    // Page contents are not cached, but a repeating next link must still not loop.
                    return next != null && visited.add(next)
                            ? fetchPage(next, remaining, pages, type, dfe)
                            : Mono.empty();
                })
                .then()
                .doOnSuccess(ignored -> log.info(
                        "Relation pages: collection={} wanted={} notFound={} pages={} pageSize={}",
                        LogSanitizer.singleLine(collection), wanted.size(), remaining.size(), pages.get(), pageSize))
                .onErrorResume(ex -> {
                    log.warn("Relation pages failed, requesting the remaining {} of {} individually: collection={} pages={} cause={}",
                            remaining.size(), wanted.size(), LogSanitizer.singleLine(collection), pages.get(),
                            LogSanitizer.singleLine(String.valueOf(ex.getMessage())));
                    return Mono.empty();
                });
    }

    private <T> Mono<JsonNode> fetchPage(String uri, Map<String, Set<String>> remaining, AtomicInteger pages,
                                         Class<T> type, DataFetchingEnvironment dfe) {
        return webClientRequest.getUncached(uri, JsonNode.class, dfe)
                .doOnNext(page -> {
                    pages.incrementAndGet();
                    collectWanted(page, remaining, type, dfe);
                });
    }

    private <T> void collectWanted(JsonNode page, Map<String, Set<String>> remaining, Class<T> type,
                                   DataFetchingEnvironment dfe) {
        for (JsonNode entry : page.path("_embedded").path("_entries")) {
            T resource = null;
            for (JsonNode self : entry.path("_links").path("self")) {
                Set<String> hrefs = parse(self.path("href").asString(""))
                        .map(id -> remaining.remove(id.id()))
                        .orElse(null);
                if (hrefs == null) {
                    continue;
                }
                if (resource == null) {
                    resource = objectMapper.treeToValue(entry, type);
                }
                for (String href : hrefs) {
                    webClientRequest.primeQueryCache(href, type, resource, dfe);
                }
            }
        }
    }

    private String nextPage(JsonNode page) {
        return StringUtils.trimToNull(page.path("_links").path("next").path(0).path("href").asString(""));
    }

    /**
     * Reads {@code <collection>/<idField>/<value>}. The path is split before decoding, since a
     * value may contain an encoded slash, and only the value is case sensitive.
     */
    private static Optional<ResourceId> parse(String href) {
        if (StringUtils.isBlank(href)) {
            return Optional.empty();
        }
        String path;
        try {
            path = URI.create(href).getRawPath();
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
        int valueSlash = StringUtils.lastIndexOf(path, "/");
        int fieldSlash = valueSlash > 0 ? path.lastIndexOf('/', valueSlash - 1) : -1;
        if (fieldSlash <= 0 || valueSlash == fieldSlash + 1 || valueSlash == path.length() - 1) {
            return Optional.empty();
        }
        String field = path.substring(fieldSlash + 1, valueSlash).toLowerCase(Locale.ROOT);
        String value = UriUtils.decode(path.substring(valueSlash + 1), StandardCharsets.UTF_8);
        return Optional.of(new ResourceId(path.substring(0, fieldSlash), field + "/" + value));
    }

    private record ResourceId(String collection, String id) { }
}
