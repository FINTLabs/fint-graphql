package no.fint.graphql.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalInt;

/**
 * When relations are read from collection pages instead of one resource at a time.
 *
 * <p>{@code threshold} applies per parent to list relations. A relation listed under
 * {@code relations}, as {@code <parent type>: <relation>: <threshold>}, instead has its links
 * counted across all parents at the same level of a query. This is what makes it work for a
 * single-valued relation, where every parent only ever sees one link.
 */
@ConfigurationProperties("fint.graphql.relation-pagination")
@Component
@Getter
public class RelationPaginationSettings {
    private int threshold = 50;
    private int pageSize = 500;
    private Map<String, Map<String, Integer>> relations = Map.of();

    public void setThreshold(int threshold) {
        this.threshold = requireNotNegative("threshold", threshold);
    }

    public void setPageSize(int pageSize) {
        // The client API answers size=0 with the whole collection, so it can never mean "a page".
        if (pageSize <= 0) {
            throw new IllegalArgumentException("fint.graphql.relation-pagination.page-size must be positive");
        }
        this.pageSize = pageSize;
    }

    public void setRelations(Map<String, Map<String, Integer>> relations) {
        Map<String, Map<String, Integer>> normalized = new HashMap<>();
        relations.forEach((parent, thresholds) -> thresholds.forEach((relation, value) -> {
            String name = "relations." + parent + "." + relation;
            if (value == null) {
                throw new IllegalArgumentException("fint.graphql.relation-pagination." + name + " must have a value");
            }
            normalized.computeIfAbsent(normalize(parent), ignored -> new HashMap<>())
                    .put(normalize(relation), requireNotNegative(name, value));
        }));
        this.relations = Map.copyOf(normalized);
    }

    public OptionalInt configuredThreshold(String parent, String relation) {
        Integer value = relations.getOrDefault(normalize(parent), Map.of()).get(normalize(relation));
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    private static int requireNotNegative(String name, int value) {
        if (value < 0) {
            throw new IllegalArgumentException("fint.graphql.relation-pagination." + name + " must not be negative");
        }
        return value;
    }

    // Environment variables can only reach these keys in lower case.
    private static String normalize(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}
