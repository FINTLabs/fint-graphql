package no.fint.graphql;

import graphql.servlet.context.GraphQLServletContext;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicLong;

public final class GraphQLRequestAttributes {
    public static final String QUERY_ID = "graphql.query.id";
    public static final String QUERY_START_NANOS = "graphql.query.startNanos";
    public static final String REQUEST_COUNTER = "graphql.query.requestCounter";

    private GraphQLRequestAttributes() {
    }

    public static Long getQueryId(GraphQLServletContext context) {
        HttpServletRequest request = getRequest(context);
        if (request == null) {
            return null;
        }
        Object value = request.getAttribute(QUERY_ID);
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    public static long nextRequestSequence(GraphQLServletContext context) {
        HttpServletRequest request = getRequest(context);
        if (request == null) {
            return -1;
        }
        AtomicLong counter = (AtomicLong) request.getAttribute(REQUEST_COUNTER);
        if (counter == null) {
            counter = new AtomicLong();
            request.setAttribute(REQUEST_COUNTER, counter);
        }
        return counter.incrementAndGet();
    }

    public static Long getQueryStartNanos(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object value = request.getAttribute(QUERY_START_NANOS);
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    private static HttpServletRequest getRequest(GraphQLServletContext context) {
        if (context == null) {
            return null;
        }
        return context.getHttpServletRequest();
    }
}
