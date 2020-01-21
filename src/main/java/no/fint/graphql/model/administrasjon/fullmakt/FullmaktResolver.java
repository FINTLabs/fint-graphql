
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonFullmaktResolver")
public class FullmaktResolver implements GraphQLResolver<FullmaktResource> {

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private RolleService rolleService;


    public CompletionStage<PersonalressursResource> getStedfortreder(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getStedfortreder()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getFullmektig(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getFullmektig()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<RolleResource> getRolle(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fullmakt.getRolle()
                .stream()
                .map(Link::getHref)
                .map(l -> rolleService.getRolleResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

}

