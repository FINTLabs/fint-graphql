package no.fint.graphql;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import no.fint.graphql.dataloader.ResourceRequestKey;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/** Query-owned state captured before leaving the servlet thread. */
public final class GraphQLQueryContext {
    @Getter private final Long queryId;
    @Getter private final String authorization;
    @Getter private final String organisationId;
    @Getter private final Set<String> allowedPathPrefixes;
    @Getter private final Cache<ResourceRequestKey, Mono<Object>> requestCache;
    private final AtomicLong requestCounter = new AtomicLong();
    private final AtomicBoolean finished = new AtomicBoolean();
    private final Sinks.Empty<Void> completion = Sinks.empty();

    private GraphQLQueryContext(HttpServletRequest request, long cacheMaximumSize) {
        queryId = GraphQLRequestAttributes.getQueryId(request);
        authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        organisationId = GraphQLRequestAttributes.getOrganisationId(request);
        allowedPathPrefixes = Set.copyOf(GraphQLRequestAttributes.getAllowedPathPrefixes(request));
        requestCache = Caffeine.newBuilder().maximumSize(cacheMaximumSize).build();
    }

    public static GraphQLQueryContext capture(HttpServletRequest request, long cacheMaximumSize) {
        return new GraphQLQueryContext(request, cacheMaximumSize);
    }

    public long nextRequestSequence() {
        return requestCounter.incrementAndGet();
    }

    public boolean isFinished() {
        return finished.get();
    }

    public Mono<Void> completion() {
        return completion.asMono();
    }

    public void finish() {
        if (finished.compareAndSet(false, true)) {
            completion.tryEmitEmpty();
            requestCache.invalidateAll();
        }
    }
}
