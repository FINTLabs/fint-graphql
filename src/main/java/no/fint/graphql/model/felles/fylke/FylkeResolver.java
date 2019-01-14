// Built from tag v3.1.0

package no.fint.graphql.model.felles.fylke;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.kommune.KommuneService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.felles.kodeverk.KommuneResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("fellesFylkeResolver")
public class FylkeResolver implements GraphQLResolver<FylkeResource> {

    @Autowired
    private KommuneService kommuneService;


    public List<KommuneResource> getKommune(FylkeResource fylke, DataFetchingEnvironment dfe) {
        return fylke.getKommune()
                .stream()
                .map(Link::getHref)
                .map(l -> kommuneService.getKommuneResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

