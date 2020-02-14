
package no.fint.graphql.model.utdanning.programomrade;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.utdanningsprogram.UtdanningsprogramService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.arstrinn.ArstrinnService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningProgramomradeResolver")
public class ProgramomradeResolver implements GraphQLResolver<ProgramomradeResource> {

    @Autowired
    private UtdanningsprogramService utdanningsprogramService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private FagService fagService;

    @Autowired
    private ArstrinnService arstrinnService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<UtdanningsprogramResource> getUtdanningsprogram(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrade.getUtdanningsprogram()
                .stream()
                .map(Link::getHref)
                .map(l -> utdanningsprogramService.getUtdanningsprogramResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<ElevforholdResource>> getElevforhold(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrade.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
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

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrade.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

