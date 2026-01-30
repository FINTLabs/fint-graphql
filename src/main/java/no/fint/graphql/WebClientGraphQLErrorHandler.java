package no.fint.graphql;

import graphql.ErrorType;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
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
            return error;
        }

        ExceptionWhileDataFetching dataFetchingError = (ExceptionWhileDataFetching) error;
        Throwable exception = unwrap(dataFetchingError.getException());

        if (exception instanceof WebClientResponseException) {
            WebClientResponseException webClientException = (WebClientResponseException) exception;
            int status = webClientException.getRawStatusCode();
            if (status == 401 || status == 403 || status == 404) {
                URI uri = webClientException.getRequest() != null ? webClientException.getRequest().getURI() : null;
                return toRemoteAccessError(dataFetchingError, status, uri);
            }
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
            message = "Failed to find resource " + resourcePath;
        } else {
            message = "Remote resource access failed with status " + status + " for " + resourcePath;
        }
        return GraphqlErrorBuilder.newError()
                .message(message)
                .path(error.getPath())
                .locations(error.getLocations())
                .errorType(ErrorType.DataFetchingException)
                .build();
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
}
