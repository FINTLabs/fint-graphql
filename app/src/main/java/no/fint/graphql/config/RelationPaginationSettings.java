package no.fint.graphql.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** When relations are read from collection pages instead of one resource at a time. */
@ConfigurationProperties("fint.graphql.relation-pagination")
@Component
@Getter
public class RelationPaginationSettings {
    /** Distinct resources a query level must exceed in one collection before its pages are read. */
    private int threshold = 50;
    private int pageSize = 500;

    public void setThreshold(int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("threshold must not be negative: " + threshold);
        }
        this.threshold = threshold;
    }

    public void setPageSize(int pageSize) {
        // The client API answers size=0 with the whole collection, so it can never mean "a page".
        if (pageSize <= 0) {
            throw new IllegalArgumentException("page-size must be positive: " + pageSize);
        }
        this.pageSize = pageSize;
    }
}
