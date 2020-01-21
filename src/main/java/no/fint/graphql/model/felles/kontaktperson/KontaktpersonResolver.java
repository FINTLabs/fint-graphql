
package no.fint.graphql.model.felles.kontaktperson;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.person.PersonService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.model.resource.felles.PersonResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("fellesKontaktpersonResolver")
public class KontaktpersonResolver implements GraphQLResolver<KontaktpersonResource> {

    @Autowired
    private PersonService personService;


    public CompletionStage<List<PersonResource>> getKontaktperson(KontaktpersonResource kontaktperson, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktperson.getKontaktperson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<PersonResource> getPerson(KontaktpersonResource kontaktperson, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktperson.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

}

