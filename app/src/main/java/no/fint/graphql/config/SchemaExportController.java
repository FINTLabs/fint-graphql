package no.fint.graphql.config;

import graphql.introspection.IntrospectionQuery;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Preserves Kickstart's public introspection JSON endpoint. */
@RestController
public class SchemaExportController {
    private final Map<String, Object> schema;

    public SchemaExportController(GraphQlSource source) {
        this.schema = source.graphQl().execute(IntrospectionQuery.INTROSPECTION_QUERY).toSpecification();
    }

    @GetMapping(value = "/schema.json", produces = "application/json")
    public Map<String, Object> schema() {
        return schema;
    }
}
