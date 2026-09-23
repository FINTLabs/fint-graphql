
package no.fint.graphql.model.model.fasttillegg;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.model.lonnsart.LonnsartService;
import no.fint.graphql.model.model.personalressurs.PersonalressursService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.novari.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.novari.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.novari.fint.model.resource.administrasjon.personal.PersonalressursResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFasttilleggResolver")
public class FasttilleggResolver {

    @Autowired
    private LonnsartService lonnsartService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private PersonalressursService personalressursService;


    @SchemaMapping(typeName = "Fasttillegg", field = "lonnsart")
    public CompletionStage<LonnsartResource> getLonnsart(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fasttillegg.getLonnsart()
                .stream()
                .map(Link::getHref)
                .map(l -> lonnsartService.getLonnsartResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fasttillegg", field = "arbeidsforhold")
    public CompletionStage<ArbeidsforholdResource> getArbeidsforhold(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fasttillegg.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fasttillegg", field = "anviser")
    public CompletionStage<PersonalressursResource> getAnviser(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fasttillegg.getAnviser()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fasttillegg", field = "konterer")
    public CompletionStage<PersonalressursResource> getKonterer(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fasttillegg.getKonterer()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fasttillegg", field = "attestant")
    public CompletionStage<PersonalressursResource> getAttestant(FasttilleggResource fasttillegg, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fasttillegg.getAttestant()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

