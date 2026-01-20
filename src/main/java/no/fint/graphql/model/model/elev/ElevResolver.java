
package no.fint.graphql.model.model.elev;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.person.PersonService;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevResource;
import no.novari.fint.model.resource.felles.PersonResource;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelElevResolver")
public class ElevResolver implements GraphQLResolver<ElevResource> {

    @Autowired
    private PersonService personService;

    @Autowired
    private ElevforholdService elevforholdService;


    public CompletionStage<PersonResource> getPerson(ElevResource elev, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elev.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<ElevforholdResource>> getElevforhold(ElevResource elev, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elev.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

