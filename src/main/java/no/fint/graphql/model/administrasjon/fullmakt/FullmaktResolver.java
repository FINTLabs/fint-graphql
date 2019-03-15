
package no.fint.graphql.model.administrasjon.fullmakt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.rolle.RolleService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.fullmakt.RolleResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonFullmaktResolver")
public class FullmaktResolver implements GraphQLResolver<FullmaktResource> {

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private RolleService rolleService;


    public PersonalressursResource getStedfortreder(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return fullmakt.getStedfortreder()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getFullmektig(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return fullmakt.getFullmektig()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public RolleResource getRolle(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return fullmakt.getRolle()
                .stream()
                .map(Link::getHref)
                .map(l -> rolleService.getRolleResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

