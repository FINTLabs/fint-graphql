// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.funksjon;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonFunksjonResolver")
public class FunksjonResolver implements GraphQLResolver<FunksjonResource> {

    @Autowired
    private FunksjonService funksjonService;

    @Autowired
    private FullmaktService fullmaktService;


    public FunksjonResource getOverordnet(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return funksjon.getOverordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<FunksjonResource> getUnderordnet(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return funksjon.getUnderordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<FullmaktResource> getFullmakt(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return funksjon.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

