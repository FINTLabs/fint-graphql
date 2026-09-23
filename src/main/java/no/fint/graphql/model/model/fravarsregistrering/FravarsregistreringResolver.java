
package no.fint.graphql.model.model.fravarsregistrering;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevfravar.ElevfravarService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.faggruppe.FaggruppeService;
import no.fint.graphql.model.model.skoleressurs.SkoleressursService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsregistreringResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFravarsregistreringResolver")
public class FravarsregistreringResolver {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private FagService fagService;

    @Autowired
    private FaggruppeService faggruppeService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private ElevfravarService elevfravarService;


    @SchemaMapping(typeName = "Fravarsregistrering", field = "registrertAv")
    public CompletionStage<SkoleressursResource> getRegistrertAv(FravarsregistreringResource fravarsregistrering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsregistrering.getRegistrertAv()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fravarsregistrering", field = "fag")
    public CompletionStage<FagResource> getFag(FravarsregistreringResource fravarsregistrering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsregistrering.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fravarsregistrering", field = "faggruppe")
    public CompletionStage<FaggruppeResource> getFaggruppe(FravarsregistreringResource fravarsregistrering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsregistrering.getFaggruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> faggruppeService.getFaggruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fravarsregistrering", field = "undervisningsgruppe")
    public CompletionStage<UndervisningsgruppeResource> getUndervisningsgruppe(FravarsregistreringResource fravarsregistrering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsregistrering.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fravarsregistrering", field = "elevfravar")
    public CompletionStage<ElevfravarResource> getElevfravar(FravarsregistreringResource fravarsregistrering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsregistrering.getElevfravar()
                .stream()
                .map(Link::getHref)
                .map(l -> elevfravarService.getElevfravarResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

