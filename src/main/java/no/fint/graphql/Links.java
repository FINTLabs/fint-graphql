package no.fint.graphql;

import no.fint.model.resource.Link;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class Links {

    public static String get(List<Link> links) {
        if (links == null || links.isEmpty()) {
            return null;
        }

        return links
                .stream()
                .filter(Objects::nonNull)
                .map(Link::getHref)
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElse(null);
    }

}
