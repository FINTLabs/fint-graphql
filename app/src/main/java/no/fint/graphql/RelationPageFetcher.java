package no.fint.graphql;

import com.google.common.hash.Hashing;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.config.RelationPaginationSettings;
import no.novari.fint.model.resource.Link;
import org.apache.commons.lang3.StringUtils;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.DataLoader;
import org.springframework.graphql.execution.BatchLoaderRegistry;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    private static final String BATCH_LOADER = "relationPages";

    private final WebClientRequest webClientRequest;
    private final ObjectMapper objectMapper;
    private final RelationPaginationSettings settings;

    public RelationPageFetcher(
            WebClientRequest webClientRequest,
            ObjectMapper objectMapper,
            RelationPaginationSettings settings,
            BatchLoaderRegistry batchLoaderRegistry) {
        this.webClientRequest = webClientRequest;
        this.objectMapper = objectMapper;
        this.settings = settings;
        batchLoaderRegistry.<BatchKey, Object>forName(BATCH_LOADER).registerMappedBatchLoader(this::loadBatch);
    }

    /** For list relations. Completes when the query cache holds what the pages could provide. Never fails on a page error. */
    public <T> Mono<Void> prefetch(String parent, String relation, List<Link> links, Class<T> type,
                                   DataFetchingEnvironment dfe) {
        if (settings.configuredThreshold(parent, relation).isPresent()) {
            return loadWithSiblings(new RelationKey(parent, relation, type), links, dfe);
        }
        return walkLargeCollections(links.stream().map(Link::getHref).toList(), settings.getThreshold(), type, dfe);
    }

    /** For single-valued relations, which are only ever read from pages when configured. */
    public <T> Mono<Void> prefetchSingle(String parent, String relation, List<Link> links, Class<T> type,
                                         DataFetchingEnvironment dfe) {
        if (settings.configuredThreshold(parent, relation).isEmpty()) {
            return Mono.empty();
        }
        return loadWithSiblings(new RelationKey(parent, relation, type), links, dfe);
    }

    /**
     * Each parent's resolver only sees its own links. Going through a DataLoader holds them back
     * until every resolver at this query level has run, so they are counted together and share
     * one walk. This must be called while the resolver runs, not on subscription.
     */
    private Mono<Void> loadWithSiblings(RelationKey relation, List<Link> links, DataFetchingEnvironment dfe) {
        DataLoader<BatchKey, Object> loader = dfe.getDataLoader(BATCH_LOADER);
        CompletableFuture<?>[] loads = links.stream()
                .map(Link::getHref)
                .filter(StringUtils::isNotBlank)
                .map(href -> loader.load(new BatchKey(relation, href), dfe))
                .toArray(CompletableFuture[]::new);
        return Mono.fromFuture(CompletableFuture.allOf(loads));
    }

    private Mono<Map<BatchKey, Object>> loadBatch(Set<BatchKey> keys, BatchLoaderEnvironment environment) {
        DataFetchingEnvironment dfe = (DataFetchingEnvironment) environment.getKeyContextsList().getFirst();
        Map<RelationKey, List<String>> byRelation = keys.stream().collect(Collectors.groupingBy(
                BatchKey::relation, Collectors.mapping(BatchKey::href, Collectors.toList())));
        return Flux.fromIterable(byRelation.entrySet())
                .flatMap(batch -> {
                    RelationKey relation = batch.getKey();
                    int threshold = settings.configuredThreshold(relation.parent(), relation.name()).orElseThrow();
                    return walkLargeCollections(batch.getValue(), threshold, relation.type(), dfe);
                })
                // The loaded values are unused: resolvers read the query cache afterwards.
                .then(Mono.just(Map.of()));
    }

    private <T> Mono<Void> walkLargeCollections(List<String> hrefs, int threshold, Class<T> type,
                                                DataFetchingEnvironment dfe) {
        if (hrefs.size() <= threshold) {
            return Mono.empty();
        }
        return Flux.fromIterable(groupByCollection(hrefs).entrySet())
                .filter(group -> group.getValue().size() > threshold)
                .flatMap(group -> webClientRequest.oncePerQuery(
                        walkKey(group.getKey(), group.getValue()), type,
                        () -> walk(group.getKey(), group.getValue(), type, dfe), dfe))
                .then();
    }

    /** Resource id to the relation hrefs naming it, per collection. Links without an id stay individual. */
    private Map<String, Map<String, Set<String>>> groupByCollection(List<String> hrefs) {
        Map<String, Map<String, Set<String>>> collections = new LinkedHashMap<>();
        for (String href : hrefs) {
            parse(href).ifPresent(id -> collections
                    .computeIfAbsent(id.collection(), ignored -> new HashMap<>())
                    .computeIfAbsent(id.id(), ignored -> new LinkedHashSet<>())
                    .add(href));
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
        int pageSize = settings.getPageSize();
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

    private record RelationKey(String parent, String name, Class<?> type) { }

    private record BatchKey(RelationKey relation, String href) { }
}
