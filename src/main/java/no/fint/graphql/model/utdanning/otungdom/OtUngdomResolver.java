
package no.fint.graphql.model.utdanning.otungdom;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.person.PersonService;
import no.fint.graphql.model.utdanning.otstatus.OtStatusService;
import no.fint.graphql.model.utdanning.otenhet.OtEnhetService;
import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.ot.OtUngdomResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.kodeverk.OtStatusResource;
import no.fint.model.resource.utdanning.kodeverk.OtEnhetResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningOtUngdomResolver")
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

