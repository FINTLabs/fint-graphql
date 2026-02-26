package no.fint.graphql.config;

import graphql.GraphQLError;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QueryTimeoutGraphQLError implements GraphQLError {
    private final String message;
    private final Map<String, Object> extensions;

    public QueryTimeoutGraphQLError(long timeoutMillis) {
        if (timeoutMillis > 0L) {
            this.message = "GraphQL query timed out after " + timeoutMillis + "ms";
            this.extensions = Map.of(
                    "code", "QUERY_TIMEOUT",
                    "timeoutMs", timeoutMillis
            );
        } else {
            this.message = "GraphQL query timed out";
            this.extensions = Collections.singletonMap("code", "QUERY_TIMEOUT");
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<graphql.language.SourceLocation> getLocations() {
        return null;
    }

    @Override
    public List<Object> getPath() {
        return null;
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
