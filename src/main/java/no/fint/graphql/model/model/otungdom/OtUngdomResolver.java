
package no.fint.graphql.model.model.otungdom;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.otenhet.OtEnhetService;
import no.fint.graphql.model.model.otstatus.OtStatusService;
import no.fint.graphql.model.model.person.PersonService;
import no.fint.graphql.model.model.programomrade.ProgramomradeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.PersonResource;
import no.novari.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import no.novari.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import no.novari.fint.model.resource.utdanning.ot.OtUngdomResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelOtUngdomResolver")
public class OtUngdomResolver implements GraphQLResolver<OtUngdomResource> {

    @Autowired
    private PersonService personService;

    @Autowired
    private OtStatusService otstatusService;

    @Autowired
    private OtEnhetService otenhetService;

    @Autowired
    private ProgramomradeService programomradeService;


    public CompletionStage<PersonResource> getPerson(OtUngdomResource otungdom, DataFetchingEnvironment dfe) {
        return Flux.fromStream(otungdom.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<OtStatusResource> getStatus(OtUngdomResource otungdom, DataFetchingEnvironment dfe) {
        return Flux.fromStream(otungdom.getStatus()
                .stream()
                .map(Link::getHref)
                .map(l -> otstatusService.getOtStatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<OtEnhetResource> getEnhet(OtUngdomResource otungdom, DataFetchingEnvironment dfe) {
        return Flux.fromStream(otungdom.getEnhet()
                .stream()
                .map(Link::getHref)
                .map(l -> otenhetService.getOtEnhetResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ProgramomradeResource> getProgramomrade(OtUngdomResource otungdom, DataFetchingEnvironment dfe) {
        return Flux.fromStream(otungdom.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

