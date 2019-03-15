
package no.fint.graphql.model.utdanning.karakterskala;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningKarakterskalaResolver")
public class KarakterskalaResolver implements GraphQLResolver<KarakterskalaResource> {

    @Autowired
    private KarakterverdiService karakterverdiService;


    public List<KarakterverdiResource> getVerdi(KarakterskalaResource karakterskala, DataFetchingEnvironment dfe) {
        return karakterskala.getVerdi()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

