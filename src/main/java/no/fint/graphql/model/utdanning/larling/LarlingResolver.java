
package no.fint.graphql.model.utdanning.larling;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.person.PersonService;
import no.fint.graphql.model.felles.virksomhet.VirksomhetService;
import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.avlagtprove.AvlagtProveService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.VirksomhetResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.larling.AvlagtProveResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningLarlingResolver")
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

