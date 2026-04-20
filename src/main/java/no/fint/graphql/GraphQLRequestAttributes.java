package no.fint.graphql;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public final class GraphQLRequestAttributes {
    public static final String CLIENT_HEADER = "x-client";
    public static final String QUERY_ID = "graphql.query.id";
    public static final String QUERY_START_NANOS = "graphql.query.startNanos";
    public static final String REQUEST_COUNTER = "graphql.query.requestCounter";
    public static final String ALLOWED_PATH_PREFIXES = "graphql.query.allowedPathPrefixes";
    public static final String ORGANISATION_ID = "graphql.query.organisationId";

    private GraphQLRequestAttributes() {
    }

    public static Long getQueryId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object value = request.getAttribute(QUERY_ID);
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    public static long nextRequestSequence(HttpServletRequest request) {
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

    public static void setAllowedPathPrefixes(HttpServletRequest request, Set<String> allowedPathPrefixes) {
        if (request == null) {
            return;
        }
        if (allowedPathPrefixes == null || allowedPathPrefixes.isEmpty()) {
            request.setAttribute(ALLOWED_PATH_PREFIXES, Collections.emptySet());
            return;
        }
        request.setAttribute(ALLOWED_PATH_PREFIXES, Collections.unmodifiableSet(new LinkedHashSet<>(allowedPathPrefixes)));
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getAllowedPathPrefixes(HttpServletRequest request) {
        if (request == null) {
            return Collections.emptySet();
        }
        Object value = request.getAttribute(ALLOWED_PATH_PREFIXES);
        if (value instanceof Set<?>) {
            return (Set<String>) value;
        }
        return Collections.emptySet();
    }

    public static void setOrganisationId(HttpServletRequest request, String organisationId) {
        if (request == null) {
            return;
        }
        request.setAttribute(ORGANISATION_ID, organisationId);
    }

    public static String getOrganisationId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Object value = request.getAttribute(ORGANISATION_ID);
        return value instanceof String ? (String) value : null;
    }
}
