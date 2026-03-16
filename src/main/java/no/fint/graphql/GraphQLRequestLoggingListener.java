package no.fint.graphql;

import graphql.servlet.core.GraphQLServletListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

import static no.fint.graphql.GraphQLRequestAttributes.CLIENT_HEADER;

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
        String client = request.getHeader(CLIENT_HEADER);
        request.setAttribute(GraphQLRequestAttributes.QUERY_ID, queryId);
        request.setAttribute(GraphQLRequestAttributes.QUERY_START_NANOS, startNanos);
        request.setAttribute(GraphQLRequestAttributes.REQUEST_COUNTER, new AtomicLong());

        log.info("GraphQL query received queryId={} path={} client={}", queryId, request.getRequestURI(), client);

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
