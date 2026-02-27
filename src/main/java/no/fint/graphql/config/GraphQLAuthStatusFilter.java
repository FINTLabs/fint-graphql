package no.fint.graphql.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Rewrites GraphQL HTTP responses to 401 when the payload contains an Unauthorized
 * GraphQL error produced by auth enforcement deeper in the execution pipeline.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class GraphQLAuthStatusFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    public GraphQLAuthStatusFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isGraphQLRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingResponseWrapper wrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, wrapper);

        if (request.isAsyncStarted()) {
            request.getAsyncContext().addListener(new AsyncListener() {
                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                    handleResponse(wrapper);
                }

                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    handleResponse(wrapper);
                }

                @Override
                public void onError(AsyncEvent event) throws IOException {
                    handleResponse(wrapper);
                }

                @Override
                public void onStartAsync(AsyncEvent event) {
                    // no-op
                }
            });
            return;
        }

        handleResponse(wrapper);
    }

    private void handleResponse(ContentCachingResponseWrapper wrapper) throws IOException {
        if (wrapper.isCommitted()) {
            return;
        }
        byte[] body = wrapper.getContentAsByteArray();
        if (body.length > 0 && isJson(wrapper.getContentType())) {
            if (hasAuthError(body)) {
                wrapper.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        wrapper.copyBodyToResponse();
    }

    private boolean hasAuthError(byte[] body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode errors = root.path("errors");
            if (!errors.isArray()) {
                return false;
            }
            for (JsonNode error : errors) {
                JsonNode extensions = error.path("extensions");
                JsonNode code = extensions.path("code");
                JsonNode message = error.path("message");
                if (code.isInt() && code.asInt() == 401 && message.isTextual()
                        && "Unauthorized".equals(message.asText())) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isJson(String contentType) {
        if (contentType == null) {
            return false;
        }
        return MediaType.valueOf(contentType).isCompatibleWith(MediaType.APPLICATION_JSON);
    }

    private boolean isGraphQLRequest(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String uri = request.getRequestURI();
        return uri != null && uri.endsWith("/graphql");
    }
}
