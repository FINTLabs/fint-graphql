package no.fint.graphql;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;

import java.util.List;

public class Links {

    public static String get(FintLinks fintLinks, String rel) {
        List<Link> links = fintLinks.getLinks().get(rel);
        if (links == null || links.isEmpty()) {
            return null;
        }

        return links.get(0).getHref();
    }

}
