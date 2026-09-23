
package no.fint.graphql.model.model.kontaktperson;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.person.PersonService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.KontaktpersonResource;
import no.novari.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Controller("modelKontaktpersonResolver")
public class KontaktpersonResolver {

    @Autowired
    private PersonService personService;


    @SchemaMapping(typeName = "Kontaktperson", field = "kontaktperson")
    public CompletionStage<List<PersonResource>> getKontaktperson(KontaktpersonResource kontaktperson, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(kontaktperson.getKontaktperson()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> personService.getPersonResource(href, dfe)
                        .map(Optional::of)
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }

}

