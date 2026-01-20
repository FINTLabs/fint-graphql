
package no.fint.graphql.model.model.person;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.landkode.LandkodeService;
import no.fint.graphql.model.model.kommune.KommuneService;
import no.fint.graphql.model.model.kjonn.KjonnService;
import no.fint.graphql.model.model.person.PersonService;
import no.fint.graphql.model.model.sprak.SprakService;
import no.fint.graphql.model.model.personalressurs.PersonalressursService;
import no.fint.graphql.model.model.kontaktperson.KontaktpersonService;
import no.fint.graphql.model.model.larling.LarlingService;
import no.fint.graphql.model.model.elev.ElevService;
import no.fint.graphql.model.model.otungdom.OtUngdomService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.felles.PersonResource;
import no.novari.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.novari.fint.model.resource.felles.kodeverk.KommuneResource;
import no.novari.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.novari.fint.model.resource.felles.PersonResource;
import no.novari.fint.model.resource.felles.kodeverk.iso.SprakResource;
import no.novari.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.novari.fint.model.resource.felles.KontaktpersonResource;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import no.novari.fint.model.resource.utdanning.elev.ElevResource;
import no.novari.fint.model.resource.utdanning.ot.OtUngdomResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelPersonResolver")
public class PersonResolver implements GraphQLResolver<PersonResource> {

    @Autowired
    private LandkodeService landkodeService;

    @Autowired
    private KommuneService kommuneService;

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
    private LarlingService larlingService;

    @Autowired
    private ElevService elevService;

    @Autowired
    private OtUngdomService otungdomService;


    public CompletionStage<List<LandkodeResource>> getStatsborgerskap(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getStatsborgerskap()
                .stream()
                .map(Link::getHref)
                .map(l -> landkodeService.getLandkodeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<KommuneResource> getKommune(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getKommune()
                .stream()
                .map(Link::getHref)
                .map(l -> kommuneService.getKommuneResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
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

    public CompletionStage<List<LarlingResource>> getLarling(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getLarling()
                .stream()
                .map(Link::getHref)
                .map(l -> larlingService.getLarlingResource(l, dfe)))
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

    public CompletionStage<OtUngdomResource> getOtungdom(PersonResource person, DataFetchingEnvironment dfe) {
        return Flux.fromStream(person.getOtungdom()
                .stream()
                .map(Link::getHref)
                .map(l -> otungdomService.getOtUngdomResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

