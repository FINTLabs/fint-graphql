// Built from tag v3.1.0

package no.fint.graphql.model.felles.kommune;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.fylke.FylkeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import no.fint.model.resource.felles.kodeverk.FylkeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("fellesKommuneResolver")
public class KommuneResolver implements GraphQLResolver<KommuneResource> {

    @Autowired
    private FylkeService fylkeService;


    public FylkeResource getFylke(KommuneResource kommune, DataFetchingEnvironment dfe) {
        return kommune.getFylke()
            .stream()
            .map(Link::getHref)
            .map(l -> fylkeService.getFylkeResource(l, dfe))
            .findFirst().orElse(null);
    }

}

