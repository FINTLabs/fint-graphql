
package no.fint.graphql.model.model.programomrademedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.programomrade.ProgramomradeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomrademedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelProgramomrademedlemskapResolver")
public class ProgramomrademedlemskapResolver {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private ProgramomradeService programomradeService;


    @SchemaMapping(typeName = "Programomrademedlemskap", field = "elevforhold")
    public CompletionStage<ElevforholdResource> getElevforhold(ProgramomrademedlemskapResource programomrademedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(programomrademedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Programomrademedlemskap", field = "programomrade")
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

