
package no.fint.graphql.model.model.arstrinn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.programomrade.ProgramomradeService;
import no.fint.graphql.model.model.klasse.KlasseService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.novari.fint.model.resource.utdanning.elev.KlasseResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelArstrinnResolver")
public class ArstrinnResolver implements GraphQLResolver<ArstrinnResource> {

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private KlasseService klasseService;


    public CompletionStage<List<ProgramomradeResource>> getProgramomrade(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arstrinn.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KlasseResource>> getKlasse(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arstrinn.getKlasse()
                .stream()
                .map(Link::getHref)
                .map(l -> klasseService.getKlasseResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

