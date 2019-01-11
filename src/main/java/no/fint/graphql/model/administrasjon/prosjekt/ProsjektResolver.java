// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.prosjekt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("administrasjonProsjektResolver")
public class ProsjektResolver implements GraphQLResolver<ProsjektResource> {

    @Autowired
    private FullmaktService fullmaktService;


    public List<FullmaktResource> getFullmakt(ProsjektResource prosjekt, DataFetchingEnvironment dfe) {
        return prosjekt.getFullmakt()
            .stream()
            .map(Link::getHref)
            .map(l -> fullmaktService.getFullmaktResource(l, dfe))
            .collect(Collectors.toList());
    }

}

