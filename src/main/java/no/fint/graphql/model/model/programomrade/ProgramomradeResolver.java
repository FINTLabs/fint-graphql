
package no.fint.graphql.model.model.programomrade;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.utdanningsprogram.UtdanningsprogramService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.arstrinn.ArstrinnService;
import no.fint.graphql.model.model.programomrademedlemskap.ProgramomrademedlemskapService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelProgramomradeResolver")
public class ProgramomradeResolver implements GraphQLResolver<ProgramomradeResource> {

    @Autowired
    private UtdanningsprogramService utdanningsprogramService;

    @Autowired
    private FagService fagService;

    @Autowired
    private ArstrinnService arstrinnService;

    @Autowired
    private ProgramomrademedlemskapService programomrademedlemskapService;


    public CompletionStage<List<UtdanningsprogramResource>> getUtdanningsprogram(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrade.getUtdanningsprogram()
                .stream()
                .map(Link::getHref)
                .map(l -> utdanningsprogramService.getUtdanningsprogramResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<FagResource>> getFag(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrade.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<ArstrinnResource>> getTrinn(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrade.getTrinn()
                .stream()
                .map(Link::getHref)
                .map(l -> arstrinnService.getArstrinnResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<ProgramomrademedlemskapResource>> getGruppemedlemskap(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrade.getGruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> programomrademedlemskapService.getProgramomrademedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

