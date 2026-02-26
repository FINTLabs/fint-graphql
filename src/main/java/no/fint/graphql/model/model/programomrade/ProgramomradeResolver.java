
package no.fint.graphql.model.model.programomrade;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.arstrinn.ArstrinnService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.programomrademedlemskap.ProgramomrademedlemskapService;
import no.fint.graphql.model.model.utdanningsprogram.UtdanningsprogramService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
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

@Component("modelProgramomradeResolver")
public class ProgramomradeResolver implements GraphQLResolver<ProgramomradeResource> {

    @Autowired
    private FagService fagService;

    @Autowired
    private ArstrinnService arstrinnService;

    @Autowired
    private UtdanningsprogramService utdanningsprogramService;

    @Autowired
    private ProgramomrademedlemskapService programomrademedlemskapService;


    public CompletionStage<List<FagResource>> getFag(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(programomrade.getFag()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> fagService.getFagResource(href, dfe)
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

    public CompletionStage<List<ArstrinnResource>> getTrinn(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(programomrade.getTrinn()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> arstrinnService.getArstrinnResource(href, dfe)
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

    public CompletionStage<List<UtdanningsprogramResource>> getUtdanningsprogram(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(programomrade.getUtdanningsprogram()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> utdanningsprogramService.getUtdanningsprogramResource(href, dfe)
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

    public CompletionStage<List<ProgramomrademedlemskapResource>> getGruppemedlemskap(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(programomrade.getGruppemedlemskap()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> programomrademedlemskapService.getProgramomrademedlemskapResource(href, dfe)
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

