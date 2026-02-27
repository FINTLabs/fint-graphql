package no.fint.graphql.config;

import graphql.GraphQLError;

import java.util.List;
import java.util.Map;

public class AuthGraphQLError implements GraphQLError {
    private final String message;
    private final Map<String, Object> extensions;

    public AuthGraphQLError(String message) {
        this.message = message != null ? message : "Unauthorized";
        this.extensions = Map.of(
                "code", 401
        );
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
