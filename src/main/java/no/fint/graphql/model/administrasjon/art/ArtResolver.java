// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.art;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonArtResolver")
public class ArtResolver implements GraphQLResolver<ArtResource> {

    @Autowired
    private FullmaktService fullmaktService;


    public List<FullmaktResource> getFullmakt(ArtResource art, DataFetchingEnvironment dfe) {
        return art.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

