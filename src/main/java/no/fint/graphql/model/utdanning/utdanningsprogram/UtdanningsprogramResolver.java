
package no.fint.graphql.model.utdanning.utdanningsprogram;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningUtdanningsprogramResolver")
public class UtdanningsprogramResolver implements GraphQLResolver<UtdanningsprogramResource> {

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<List<SkoleResource>> getSkole(UtdanningsprogramResource utdanningsprogram, DataFetchingEnvironment dfe) {
        return Flux.fromStream(utdanningsprogram.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<ProgramomradeResource>> getProgramomrade(UtdanningsprogramResource utdanningsprogram, DataFetchingEnvironment dfe) {
        return Flux.fromStream(utdanningsprogram.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(UtdanningsprogramResource utdanningsprogram, DataFetchingEnvironment dfe) {
        return Flux.fromStream(utdanningsprogram.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

