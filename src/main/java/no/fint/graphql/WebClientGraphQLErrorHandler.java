package no.fint.graphql;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
            log.info("Unmapped GraphQLError: {}", error);
            return error;
        }

        ExceptionWhileDataFetching dataFetchingError = (ExceptionWhileDataFetching) error;
        Throwable exception = unwrap(dataFetchingError.getException());

        if (exception instanceof WebClientResponseException) {
            WebClientResponseException webClientException = (WebClientResponseException) exception;
            int status = webClientException.getRawStatusCode();
            URI uri = webClientException.getRequest() != null ? webClientException.getRequest().getURI() : null;
            return toRemoteAccessError(dataFetchingError, status, uri);
        }

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
