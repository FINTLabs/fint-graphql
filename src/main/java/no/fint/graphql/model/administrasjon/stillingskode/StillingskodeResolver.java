// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.stillingskode;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.stillingskode.StillingskodeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonStillingskodeResolver")
public class StillingskodeResolver implements GraphQLResolver<StillingskodeResource> {

    @Autowired
    private StillingskodeService stillingskodeService;


    public StillingskodeResource getForelder(StillingskodeResource stillingskode, DataFetchingEnvironment dfe) {
        return stillingskode.getForelder()
                .stream()
                .map(Link::getHref)
                .map(l -> stillingskodeService.getStillingskodeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

