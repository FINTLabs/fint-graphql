
package no.fint.graphql.model.model.kontaktperson;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.person.PersonService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.KontaktpersonResource;
import no.novari.fint.model.resource.felles.PersonResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelKontaktpersonResolver")
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

}

