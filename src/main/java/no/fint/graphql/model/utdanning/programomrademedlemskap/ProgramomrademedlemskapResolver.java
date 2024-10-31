
package no.fint.graphql.model.utdanning.programomrademedlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningProgramomrademedlemskapResolver")
public class ProgramomrademedlemskapResolver implements GraphQLResolver<ProgramomrademedlemskapResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private ProgramomradeService programomradeService;


    public CompletionStage<ElevforholdResource> getElevforhold(ProgramomrademedlemskapResource programomrademedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrademedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ProgramomradeResource> getProgramomrade(ProgramomrademedlemskapResource programomrademedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrademedlemskap.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

