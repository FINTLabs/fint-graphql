
package no.fint.graphql.model.model.utdanningsprogram;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.programomrade.ProgramomradeService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
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

@Controller("modelUtdanningsprogramResolver")
public class UtdanningsprogramResolver {

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ProgramomradeService programomradeService;


    @SchemaMapping(typeName = "Utdanningsprogram", field = "skole")
    public CompletionStage<List<SkoleResource>> getSkole(UtdanningsprogramResource utdanningsprogram, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(utdanningsprogram.getSkole()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> skoleService.getSkoleResource(href, dfe)
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

    @SchemaMapping(typeName = "Utdanningsprogram", field = "programomrade")
    public CompletionStage<List<ProgramomradeResource>> getProgramomrade(UtdanningsprogramResource utdanningsprogram, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(utdanningsprogram.getProgramomrade()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> programomradeService.getProgramomradeResource(href, dfe)
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

