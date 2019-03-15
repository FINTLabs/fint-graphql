
package no.fint.graphql.model.administrasjon.lonnsart;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.art.ArtService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonLonnsartResolver")
public class LonnsartResolver implements GraphQLResolver<LonnsartResource> {

    @Autowired
    private ArtService artService;


    public ArtResource getArt(LonnsartResource lonnsart, DataFetchingEnvironment dfe) {
        return lonnsart.getArt()
                .stream()
                .map(Link::getHref)
                .map(l -> artService.getArtResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

