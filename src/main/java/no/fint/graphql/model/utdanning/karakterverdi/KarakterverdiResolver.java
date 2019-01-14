// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterverdi;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.karakterskala.KarakterskalaService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningKarakterverdiResolver")
public class KarakterverdiResolver implements GraphQLResolver<KarakterverdiResource> {

    @Autowired
    private KarakterskalaService karakterskalaService;


    public KarakterskalaResource getSkala(KarakterverdiResource karakterverdi, DataFetchingEnvironment dfe) {
        return karakterverdi.getSkala()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterskalaService.getKarakterskalaResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

