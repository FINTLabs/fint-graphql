package no.fint.graphql;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GraphQLRequestInterceptor implements WebGraphQlInterceptor {
    private final GraphQLQueryIdProvider queryIdProvider;
    private final long requestCacheMaximumSize;

    public GraphQLRequestInterceptor(GraphQLQueryIdProvider queryIdProvider,
            @Value("${fint.webclient.request-cache-maximum-size:10000}") long requestCacheMaximumSize) {
        this.queryIdProvider = queryIdProvider;
        this.requestCacheMaximumSize = requestCacheMaximumSize;
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        // Capture on the servlet thread; resolvers use GraphQLContext after asynchronous handoff.
        var attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest servletRequest = attributes.getRequest();
        long queryId = queryIdProvider.nextId();
        long startNanos = System.nanoTime();
        servletRequest.setAttribute(GraphQLRequestAttributes.QUERY_ID, queryId);
        servletRequest.setAttribute(GraphQLRequestAttributes.QUERY_START_NANOS, startNanos);
        GraphQLQueryContext queryContext = GraphQLQueryContext.capture(servletRequest, requestCacheMaximumSize);
        request.configureExecutionInput((input, builder) -> builder.graphQLContext(context -> {
            context.put(GraphQLQueryContext.class, queryContext);
            context.put(GraphQLRequestAttributes.ALLOWED_PATH_PREFIXES,
                    queryContext.getAllowedPathPrefixes());
            String organisationId = queryContext.getOrganisationId();
            if (organisationId != null) {
                context.put(GraphQLRequestAttributes.ORGANISATION_ID, organisationId);
            }
        }).build());
        log.info("GraphQL query received queryId={} path={} client={} remote={}", queryId,
                LogSanitizer.singleLine(servletRequest.getRequestURI()),
                LogSanitizer.singleLine(servletRequest.getHeader(GraphQLRequestAttributes.CLIENT_HEADER)),
                LogSanitizer.singleLine(servletRequest.getRemoteAddr()));
        return chain.next(request).doFinally(signal -> {
            queryContext.finish();
            log.info("GraphQL query completed queryId={} durationMs={}",
                    queryId, (System.nanoTime() - startNanos) / 1_000_000);
        });
    }
}
