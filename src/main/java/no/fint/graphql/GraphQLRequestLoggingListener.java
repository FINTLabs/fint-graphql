package no.fint.graphql;

import graphql.servlet.core.GraphQLServletListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        request.setAttribute(GraphQLRequestAttributes.QUERY_ID, queryId);
        request.setAttribute(GraphQLRequestAttributes.QUERY_START_NANOS, startNanos);
        request.setAttribute(GraphQLRequestAttributes.REQUEST_COUNTER, new AtomicLong());

        log.info("GraphQL request received id={} path={} remote={}", queryId, request.getRequestURI(), request.getRemoteAddr());

        return new RequestCallback() {
            @Override
            public void onFinally(HttpServletRequest req, HttpServletResponse res) {
                Long started = GraphQLRequestAttributes.getQueryStartNanos(req);
                long durationMs = started != null ? (System.nanoTime() - started) / 1_000_000 : -1;
                log.info("GraphQL request completed id={} durationMs={}", queryId, durationMs);
            }
        };
    }
}
