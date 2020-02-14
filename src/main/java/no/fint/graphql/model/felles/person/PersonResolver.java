
package no.fint.graphql.model.felles.person;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.landkode.LandkodeService;
import no.fint.graphql.model.felles.kjonn.KjonnService;
import no.fint.graphql.model.felles.person.PersonService;
import no.fint.graphql.model.felles.sprak.SprakService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.felles.kontaktperson.KontaktpersonService;
import no.fint.graphql.model.utdanning.elev.ElevService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.model.resource.utdanning.elev.ElevResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("fellesPersonResolver")
public class PersonResolver implements GraphQLResolver<PersonResource> {

    @Autowired
    private LandkodeService landkodeService;

    @Autowired
    private KjonnService kjonnService;

    @Autowired
    private PersonService personService;

    @Autowired
    private SprakService sprakService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private KontaktpersonService kontaktpersonService;

    @Autowired
    private ElevService elevService;


    public CompletionStage<List<LandkodeResource>> getStatsborgerskap(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getStatsborgerskap()
                .stream()
                .map(Link::getHref)
                .map(l -> landkodeService.getLandkodeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<KjonnResource> getKjonn(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getKjonn()
                .stream()
                .map(Link::getHref)
                .map(l -> kjonnService.getKjonnResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<PersonResource>> getForeldreansvar(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getForeldreansvar()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<SprakResource> getMalform(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getMalform()
                .stream()
                .map(Link::getHref)
                .map(l -> sprakService.getSprakResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getPersonalressurs(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getPersonalressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SprakResource> getMorsmal(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getMorsmal()
                .stream()
                .map(Link::getHref)
                .map(l -> sprakService.getSprakResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<KontaktpersonResource>> getParorende(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getParorende()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktpersonService.getKontaktpersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<PersonResource>> getForeldre(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getForeldre()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<ElevResource> getElev(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevService.getElevResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

