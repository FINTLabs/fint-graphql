
package no.fint.graphql.model.model.larling;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.person.PersonService;
import no.fint.graphql.model.model.virksomhet.VirksomhetService;
import no.fint.graphql.model.model.programomrade.ProgramomradeService;
import no.fint.graphql.model.model.avlagtprove.AvlagtProveService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import no.novari.fint.model.resource.felles.PersonResource;
import no.novari.fint.model.resource.felles.VirksomhetResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.novari.fint.model.resource.utdanning.larling.AvlagtProveResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelLarlingResolver")
public class LarlingResolver implements GraphQLResolver<LarlingResource> {

    @Autowired
    private PersonService personService;

    @Autowired
    private VirksomhetService virksomhetService;

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private AvlagtProveService avlagtproveService;


    public CompletionStage<PersonResource> getPerson(LarlingResource larling, DataFetchingEnvironment dfe) {
        return Flux.fromStream(larling.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<VirksomhetResource> getBedrift(LarlingResource larling, DataFetchingEnvironment dfe) {
        return Flux.fromStream(larling.getBedrift()
                .stream()
                .map(Link::getHref)
                .map(l -> virksomhetService.getVirksomhetResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ProgramomradeResource> getProgramomrade(LarlingResource larling, DataFetchingEnvironment dfe) {
        return Flux.fromStream(larling.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<AvlagtProveResource>> getAvlagtprove(LarlingResource larling, DataFetchingEnvironment dfe) {
        return Flux.fromStream(larling.getAvlagtprove()
                .stream()
                .map(Link::getHref)
                .map(l -> avlagtproveService.getAvlagtProveResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

