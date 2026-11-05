package no.fint.graphql;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.config.QueryTimeoutGraphQLError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.spi.LoggingEventBuilder;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
        AtomicReference<String> outcome = new AtomicReference<>();
        return chain.next(request)
                .doOnNext(response -> outcome.set(outcomeOf(response)))
                .doFinally(signal -> {
                    queryContext.finish();
                    logCompleted(request, queryContext, (System.nanoTime() - startNanos) / 1_000_000,
                            outcome.get() != null ? outcome.get() : outcomeOf(signal));
                });
    }

    private static String outcomeOf(WebGraphQlResponse response) {
        if (response.getExecutionResult().getErrors().stream().anyMatch(QueryTimeoutGraphQLError.class::isInstance)) {
            return "timeout";
        }
        if (response.getErrors().isEmpty()) {
            return "ok";
        }
        return response.isValid() ? "partial" : "error";
    }

    private static String outcomeOf(SignalType signal) {
        return switch (signal) {
            case CANCEL -> "cancelled";
            case ON_ERROR -> "error";
            default -> "unknown";
        };
    }

    /**
     * Logs one line for each finished query, with the values as separate JSON fields. Use queryUid
     * to find one specific query, because queryId starts over when the application restarts.
     */
    private void logCompleted(WebGraphQlRequest request, GraphQLQueryContext query, long durationMs, String outcome) {
        Optional<QuerySignature> signature = QuerySignature.of(request.getDocument(), request.getOperationName());
        LoggingEventBuilder event = log.atInfo()
                .setMessage("GraphQL query completed queryId={} durationMs={}")
                .addArgument(query.getQueryId())
                .addArgument(durationMs)
                .addKeyValue("queryUid", UUID.randomUUID().toString())
                .addKeyValue("durationMs", durationMs)
                .addKeyValue("outcome", outcome)
                .addKeyValue("requestCount", query.getRequestCount())
                .addKeyValue("client", LogSanitizer.singleLine(query.getClientName()))
                .addKeyValue("organisation", LogSanitizer.singleLine(query.getOrganisationId()));
        signature.ifPresent(value -> event
                .addKeyValue("operation", LogSanitizer.singleLine(value.operation()))
                .addKeyValue("queryHash", value.hash())
                .addKeyValue("query", value.text()));
        event.log();
    }
}
