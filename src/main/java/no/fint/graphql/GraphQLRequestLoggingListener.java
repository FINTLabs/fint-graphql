package no.fint.graphql;

import graphql.kickstart.servlet.core.GraphQLServletListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class GraphQLRequestLoggingListener implements GraphQLServletListener {
    private final GraphQLQueryIdProvider queryIdProvider;

    public GraphQLRequestLoggingListener(GraphQLQueryIdProvider queryIdProvider) {
        this.queryIdProvider = queryIdProvider;
    }

    @Override
    public RequestCallback onRequest(HttpServletRequest request, HttpServletResponse response) {
        long queryId = queryIdProvider.nextId();
        long startNanos = System.nanoTime();
        String client = request.getHeader(GraphQLRequestAttributes.CLIENT_HEADER);
        request.setAttribute(GraphQLRequestAttributes.QUERY_ID, queryId);
        request.setAttribute(GraphQLRequestAttributes.QUERY_START_NANOS, startNanos);
        request.setAttribute(GraphQLRequestAttributes.REQUEST_COUNTER, new AtomicLong());

        log.info("GraphQL query received queryId={} path={} client={} remote={}",
                queryId, request.getRequestURI(), client, request.getRemoteAddr());

        return new RequestCallback() {
            @Override
            public void onFinally(HttpServletRequest req, HttpServletResponse res) {
                Long started = GraphQLRequestAttributes.getQueryStartNanos(req);
                long durationMs = started != null ? (System.nanoTime() - started) / 1_000_000 : -1;
                log.info("GraphQL query completed queryId={} durationMs={}", queryId, durationMs);
            }
        };
    }
}
