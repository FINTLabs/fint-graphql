
package no.fint.graphql.model.model.rom;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.eksamen.EksamenService;
import no.fint.graphql.model.model.time.TimeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.timeplan.EksamenResource;
import no.novari.fint.model.resource.utdanning.timeplan.RomResource;
import no.novari.fint.model.resource.utdanning.timeplan.TimeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component("modelRomResolver")
public class RomResolver implements GraphQLResolver<RomResource> {

    @Autowired
    private TimeService timeService;

    @Autowired
    private EksamenService eksamenService;


    public CompletionStage<List<TimeResource>> getTime(RomResource rom, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(rom.getTime()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> timeService.getTimeResource(href, dfe)
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

    public CompletionStage<List<EksamenResource>> getEksamen(RomResource rom, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(rom.getEksamen()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> eksamenService.getEksamenResource(href, dfe)
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

