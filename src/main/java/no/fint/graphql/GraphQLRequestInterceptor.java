package no.fint.graphql;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GraphQLRequestInterceptor implements WebGraphQlInterceptor {
    private final GraphQLQueryIdProvider queryIdProvider;

    public GraphQLRequestInterceptor(GraphQLQueryIdProvider queryIdProvider) {
        this.queryIdProvider = queryIdProvider;
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
        servletRequest.setAttribute(GraphQLRequestAttributes.REQUEST_COUNTER, new AtomicLong());
        request.configureExecutionInput((input, builder) -> builder.graphQLContext(context -> {
            context.put(HttpServletRequest.class, servletRequest);
            context.put(GraphQLRequestAttributes.ALLOWED_PATH_PREFIXES,
                    GraphQLRequestAttributes.getAllowedPathPrefixes(servletRequest));
            String organisationId = GraphQLRequestAttributes.getOrganisationId(servletRequest);
            if (organisationId != null) {
                context.put(GraphQLRequestAttributes.ORGANISATION_ID, organisationId);
            }
        }).build());
        log.info("GraphQL query received queryId={} path={} client={} remote={}", queryId,
                servletRequest.getRequestURI(), servletRequest.getHeader(GraphQLRequestAttributes.CLIENT_HEADER),
                servletRequest.getRemoteAddr());
        return chain.next(request).doFinally(signal -> log.info(
                "GraphQL query completed queryId={} durationMs={}", queryId, (System.nanoTime() - startNanos) / 1_000_000));
    }
}
