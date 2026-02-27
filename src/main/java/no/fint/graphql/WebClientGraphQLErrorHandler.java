package no.fint.graphql;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.internal.shaded.reactor.pool.PoolAcquirePendingLimitException;
import reactor.netty.internal.shaded.reactor.pool.PoolAcquireTimeoutException;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebClientGraphQLErrorHandler extends DefaultGraphQLErrorHandler {

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        List<GraphQLError> mapped = errors.stream()
                .map(this::mapWebClientError)
                .collect(Collectors.toList());
        return super.processErrors(mapped);
    }

    private GraphQLError mapWebClientError(GraphQLError error) {
        if (!(error instanceof ExceptionWhileDataFetching)) {
            log.warn("Unmapped GraphQLError: {}", error);
            return error;
        }

        ExceptionWhileDataFetching dataFetchingError = (ExceptionWhileDataFetching) error;
        Throwable exception = unwrap(dataFetchingError.getException());
        logDataFetchingError(dataFetchingError, exception);

        if (exception instanceof WebClientResponseException) {
            WebClientResponseException webClientException = (WebClientResponseException) exception;
            int status = webClientException.getRawStatusCode();
            URI uri = webClientException.getRequest() != null ? webClientException.getRequest().getURI() : null;
            return toRemoteAccessError(dataFetchingError, status, uri);
        }
        if (exception instanceof WebClientRequestException) {
            WebClientRequestException requestException = (WebClientRequestException) exception;
            String resourcePath = resourcePath(requestException.getUri());
            return toRemoteFailureError(dataFetchingError, requestException, resourcePath);
        }
        if (exception instanceof MissingAuthorizationException) {
            return new RemoteAccessGraphQLError(
                    "Unauthorized",
                    error.getLocations(),
                    error.getPath(),
                    Map.of(
                            "code", 401
                    )
            );
        }

        log.warn("Unmapped ExceptionWhileDataFetching: {}", error);
        return error;
    }

    private GraphQLError toRemoteAccessError(ExceptionWhileDataFetching error, int status, URI uri) {
        String resourcePath = uri != null ? uri.getPath() : "unknown resource";
        String message;
        if (status == 401) {
            message = "Access unauthorized for " + resourcePath;
        } else if (status == 403) {
            message = "Access forbidden for " + resourcePath;
        } else if (status == 404) {
            message = "Resource not found at " + resourcePath;
        } else {
            message = HttpStatus.resolve(status).getReasonPhrase() + " for " + resourcePath;
        }
        return new RemoteAccessGraphQLError(
                message,
                error.getLocations(),
                error.getPath(),
                buildExtensions(status, resourcePath)
        );
    }

    private GraphQLError toRemoteFailureError(ExceptionWhileDataFetching error,
                                              WebClientRequestException exception,
                                              String resourcePath) {
        String message = failureMessage(exception, resourcePath);
        return new RemoteAccessGraphQLError(
                message,
                error.getLocations(),
                error.getPath(),
                buildExtensions(resourcePath, exception)
        );
    }

    private Map<String, Object> buildExtensions(int status, String resourcePath) {
        Map<String, Object> extensions = new LinkedHashMap<>();
        extensions.put("code", status);
        String[] parts = resourcePath != null ? resourcePath.split("/") : new String[0];
        if (hasResourcePattern(parts)) {
            // parts[0] is empty when path starts with '/'
            extensions.put("domain", getPart(parts, 1));
            extensions.put("package", getPart(parts, 2));
            extensions.put("resource", getPart(parts, 3));
            extensions.put("idkey", getPart(parts, 4));
            extensions.put("idvalue", getPart(parts, 5));
        } else {
            extensions.put("code", status);
        }
        return extensions;
    }

    private Map<String, Object> buildExtensions(String resourcePath, WebClientRequestException exception) {
        Map<String, Object> extensions = new LinkedHashMap<>();
        String[] parts = resourcePath != null ? resourcePath.split("/") : new String[0];
        if (hasResourcePattern(parts)) {
            extensions.put("domain", getPart(parts, 1));
            extensions.put("package", getPart(parts, 2));
            extensions.put("resource", getPart(parts, 3));
            extensions.put("idkey", getPart(parts, 4));
            extensions.put("idvalue", getPart(parts, 5));
        }
        if (exception.getQueryId() != null) {
            extensions.put("queryId", exception.getQueryId());
        }
        if (exception.getRequestId() != null) {
            extensions.put("requestId", exception.getRequestId());
        }
        if (exception.getUri() != null) {
            extensions.put("uri", exception.getUri());
        }
        Throwable root = findRelevantCause(exception);
        extensions.put("cause", root.getClass().getSimpleName());
        return extensions;
    }

    private boolean hasResourcePattern(String[] parts) {
        return getPart(parts, 1) != null
                && getPart(parts, 2) != null
                && getPart(parts, 3) != null
                && getPart(parts, 4) != null
                && getPart(parts, 5) != null
                && parts.length <= 6;
    }

    private String getPart(String[] parts, int index) {
        return parts.length > index && !parts[index].isEmpty() ? parts[index] : null;
    }

    private Throwable unwrap(Throwable exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof WebClientResponseException) {
                return current;
            }
            if (current instanceof WebClientRequestException) {
                return current;
            }
            current = current.getCause();
        }
        return exception;
    }

    private void logDataFetchingError(ExceptionWhileDataFetching error, Throwable exception) {
        if (exception instanceof WebClientRequestException) {
            WebClientRequestException requestException = (WebClientRequestException) exception;
            log.error("GraphQL data fetch failed path={} uri={} queryId={} requestId={} message={}",
                    error.getPath(),
                    requestException.getUri(),
                    requestException.getQueryId(),
                    requestException.getRequestId(),
                    requestException.getMessage(),
                    requestException);
            return;
        }
        log.error("GraphQL data fetch failed path={} message={}",
                error.getPath(),
                exception.getMessage(),
                exception);
    }

    private String failureMessage(WebClientRequestException exception, String resourcePath) {
        String target = resourcePath != null ? resourcePath : "unknown resource";
        Throwable root = findRelevantCause(exception);
        if (root instanceof PoolAcquireTimeoutException || root instanceof PoolAcquirePendingLimitException) {
            return "Web request delayed by connection pool exhaustion for " + target;
        }
        if (root instanceof ConnectTimeoutException) {
            return "Web request connection timeout for " + target;
        }
        if (root instanceof ReadTimeoutException || root instanceof WriteTimeoutException) {
            return "Web request response timeout for " + target;
        }
        return "Web request failed for " + target;
    }

    private String resourcePath(String uri) {
        if (uri == null) {
            return "unknown resource";
        }
        try {
            return URI.create(uri).getPath();
        } catch (IllegalArgumentException ex) {
            return uri;
        }
    }

    private Throwable findRelevantCause(Throwable exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof PoolAcquireTimeoutException
                    || current instanceof PoolAcquirePendingLimitException
                    || current instanceof ConnectTimeoutException
                    || current instanceof ReadTimeoutException
                    || current instanceof WriteTimeoutException) {
                return current;
            }
            current = current.getCause();
        }
        return exception;
    }

    private static final class RemoteAccessGraphQLError implements GraphQLError {
        private final String message;
        private final List<graphql.language.SourceLocation> locations;
        private final List<Object> path;
        private final Map<String, Object> extensions;

        private RemoteAccessGraphQLError(
                String message,
                List<graphql.language.SourceLocation> locations,
                List<Object> path,
                Map<String, Object> extensions
        ) {
            this.message = message;
            this.locations = locations;
            this.path = path;
            this.extensions = extensions != null ? Collections.unmodifiableMap(extensions) : null;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public List<graphql.language.SourceLocation> getLocations() {
            return locations;
        }

        @Override
        public List<Object> getPath() {
            return path;
        }

        @Override
        public Map<String, Object> getExtensions() {
            return extensions;
        }

        @Override
        public graphql.ErrorClassification getErrorType() {
            return null;
        }
    }
}
