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
import java.util.stream.Collectors;

/**
 * Loads large relations by walking the target collection's pages instead of requesting every
 * resource on its own. Found resources are placed in the query cache under the relation's own
 * link, so the resolver's ordinary per-link lookup picks them up. Anything a walk does not find,
 * for any reason, is simply requested on its own as before.
 *
 * <p>A resolver only sees the links of one parent, so one elev per elevforhold would never look
 * large. All links therefore go through a DataLoader, which holds them back until every resolver
 * at the same query level has run. The threshold then counts the distinct resources wanted from
 * a collection across all parents, and they share one walk.
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

    /**
     * Completes when the query cache holds what the pages could provide. Never fails on a page
     * error. The links are registered immediately, because the DataLoader must see them while the
     * resolver runs and not on subscription.
     */
    public <T> Mono<Void> prefetch(List<Link> links, Class<T> type, DataFetchingEnvironment dfe) {
        DataLoader<BatchKey, Object> loader = dfe.getDataLoader(BATCH_LOADER);
        CompletableFuture<?>[] loads = links.stream()
                .map(Link::getHref)
                .filter(StringUtils::isNotBlank)
                .map(href -> loader.load(new BatchKey(href, type), dfe))
                .toArray(CompletableFuture[]::new);
        return Mono.fromFuture(CompletableFuture.allOf(loads));
    }

    private Mono<Map<BatchKey, Object>> loadBatch(Set<BatchKey> keys, BatchLoaderEnvironment environment) {
        DataFetchingEnvironment dfe = (DataFetchingEnvironment) environment.getKeyContextsList().getFirst();
        Map<Class<?>, List<String>> hrefsByType = keys.stream().collect(Collectors.groupingBy(
                BatchKey::type, Collectors.mapping(BatchKey::href, Collectors.toList())));
        return Flux.fromIterable(hrefsByType.entrySet())
                .flatMap(batch -> walkLargeCollections(batch.getValue(), batch.getKey(), dfe))
                // The loaded values are unused: resolvers read the query cache afterwards.
                .then(Mono.just(Map.of()));
    }

    private <T> Mono<Void> walkLargeCollections(List<String> hrefs, Class<T> type, DataFetchingEnvironment dfe) {
        return Flux.fromIterable(groupByCollection(hrefs).entrySet())
                .filter(group -> group.getValue().size() > settings.getThreshold())
                .flatMap(group -> webClientRequest.oncePerQuery(
                        walkKey(group.getKey(), group.getValue()), type,
                        () -> new Walk<>(group.getKey(), group.getValue(), type, dfe).run(), dfe))
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

    private String walkKey(String collection, Map<String, Set<String>> wanted) {
        String ids = new TreeSet<>(wanted.keySet()).toString();
        return "relation-pages:" + collection + ":" + Hashing.murmur3_128().hashUnencodedChars(ids);
    }

    /** One pass over a collection's pages. Pages are read one at a time, so its state needs no locking. */
    private final class Walk<T> {
        private final String collection;
        private final int wantedCount;
        private final Map<String, Set<String>> remaining;
        private final Class<T> type;
        private final DataFetchingEnvironment dfe;
        private final int pageSize = settings.getPageSize();
        private final Set<String> visited = new HashSet<>();
        private int pages;
        private long entriesSeen;

        private Walk(String collection, Map<String, Set<String>> wanted, Class<T> type, DataFetchingEnvironment dfe) {
            this.collection = collection;
            this.wantedCount = wanted.size();
            this.remaining = new HashMap<>(wanted);
            this.type = type;
            this.dfe = dfe;
        }

        private Mono<Void> run() {
            return fetchPage(collection + "?size=" + pageSize)
                    .expand(page -> {
                        String next = nextPage(page);
                        return next == null || remaining.isEmpty() || individualRequestsAreCheaper(page)
                                ? Mono.empty()
                                : fetchPage(next);
                    })
                    .then()
                    .doOnSuccess(ignored -> log.info(
                            "Relation pages: collection={} wanted={} individually={} pages={} pageSize={}",
                            LogSanitizer.singleLine(collection), wantedCount, remaining.size(), pages, pageSize))
                    .onErrorResume(ex -> {
                        log.warn("Relation pages failed, requesting the remaining {} of {} individually: collection={} pages={} cause={}",
                                remaining.size(), wantedCount, LogSanitizer.singleLine(collection), pages,
                                LogSanitizer.singleLine(String.valueOf(ex.getMessage())));
                        return Mono.empty();
                    });
        }

        private Mono<JsonNode> fetchPage(String uri) {
            // Page contents are not cached, but a repeating next link must still not loop.
            if (!visited.add(uri)) {
                return Mono.empty();
            }
            return webClientRequest.getUncached(uri, JsonNode.class, dfe).doOnNext(page -> {
                pages++;
                collectWanted(page);
            });
        }

        private void collectWanted(JsonNode page) {
            for (JsonNode entry : page.path("_embedded").path("_entries")) {
                entriesSeen++;
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

        /**
         * Finding a few resources should not cost many pages, for example 60 elever out of 50,000.
         * Checked after every page, since the answer changes as the walk finds what it wants.
         */
        private boolean individualRequestsAreCheaper(JsonNode page) {
            long totalItems = page.path("total_items").asLong(-1);
            return totalItems >= 0 && Math.ceilDiv(totalItems - entriesSeen, pageSize) >= remaining.size();
        }

        private String nextPage(JsonNode page) {
            return StringUtils.trimToNull(page.path("_links").path("next").path(0).path("href").asString(""));
        }
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

    private record BatchKey(String href, Class<?> type) { }
}
