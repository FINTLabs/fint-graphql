
package no.fint.graphql.model.model.larling;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.avlagtprove.AvlagtProveService;
import no.fint.graphql.model.model.person.PersonService;
import no.fint.graphql.model.model.programomrade.ProgramomradeService;
import no.fint.graphql.model.model.virksomhet.VirksomhetService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.PersonResource;
import no.novari.fint.model.resource.felles.VirksomhetResource;
import no.novari.fint.model.resource.utdanning.larling.AvlagtProveResource;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
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

@Controller("modelLarlingResolver")
public class LarlingResolver {

    @Autowired
    private PersonService personService;

    @Autowired
    private VirksomhetService virksomhetService;

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private AvlagtProveService avlagtproveService;


    @SchemaMapping(typeName = "Larling", field = "person")
    public CompletionStage<PersonResource> getPerson(LarlingResource larling, DataFetchingEnvironment dfe) {
        return Flux.fromStream(larling.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Larling", field = "bedrift")
    public CompletionStage<VirksomhetResource> getBedrift(LarlingResource larling, DataFetchingEnvironment dfe) {
        return Flux.fromStream(larling.getBedrift()
                .stream()
                .map(Link::getHref)
                .map(l -> virksomhetService.getVirksomhetResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Larling", field = "programomrade")
    public CompletionStage<ProgramomradeResource> getProgramomrade(LarlingResource larling, DataFetchingEnvironment dfe) {
        return Flux.fromStream(larling.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Larling", field = "avlagtprove")
    public CompletionStage<List<AvlagtProveResource>> getAvlagtprove(LarlingResource larling, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(larling.getAvlagtprove()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> avlagtproveService.getAvlagtProveResource(href, dfe)
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

