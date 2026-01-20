
package no.fint.graphql.model.model.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.person.PersonService;
import no.fint.graphql.model.model.personalressurs.PersonalressursService;
import no.fint.graphql.model.model.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.sensor.SensorService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.felles.PersonResource;
import no.novari.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.utdanning.vurdering.SensorResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelSkoleressursResolver")
public class SkoleressursResolver implements GraphQLResolver<SkoleressursResource> {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private SensorService sensorService;


    public CompletionStage<PersonResource> getPerson(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skoleressurs.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getPersonalressurs(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skoleressurs.getPersonalressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skoleressurs.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SkoleResource>> getSkole(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skoleressurs.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SensorResource>> getSensor(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skoleressurs.getSensor()
                .stream()
                .map(Link::getHref)
                .map(l -> sensorService.getSensorResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

