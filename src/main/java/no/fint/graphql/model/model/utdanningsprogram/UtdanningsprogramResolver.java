
package no.fint.graphql.model.model.utdanningsprogram;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.programomrade.ProgramomradeService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelUtdanningsprogramResolver")
public class UtdanningsprogramResolver implements GraphQLResolver<UtdanningsprogramResource> {

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ProgramomradeService programomradeService;


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

}

