// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fravarstype;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonFravarstypeResolver")
public class FravarstypeResolver implements GraphQLResolver<FravarstypeResource> {

    @Autowired
    private LonnsartService lonnsartService;


    public LonnsartResource getLonnsart(FravarstypeResource fravarstype, DataFetchingEnvironment dfe) {
        return fravarstype.getLonnsart()
                .stream()
                .map(Link::getHref)
                .map(l -> lonnsartService.getLonnsartResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

