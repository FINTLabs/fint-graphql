// Built from tag v3.1.0

package no.fint.graphql.model.felles.adresse;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.landkode.LandkodeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.kompleksedatatyper.AdresseResource;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("fellesAdresseResolver")
public class AdresseResolver implements GraphQLResolver<AdresseResource> {

    @Autowired
    private LandkodeService landkodeService;


    public LandkodeResource getLand(AdresseResource adresse, DataFetchingEnvironment dfe) {
        return adresse.getLand()
                .stream()
                .map(Link::getHref)
                .map(l -> landkodeService.getLandkodeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

