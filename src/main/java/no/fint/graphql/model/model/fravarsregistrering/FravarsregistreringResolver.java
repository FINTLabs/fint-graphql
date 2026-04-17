
package no.fint.graphql.model.model.fravarsregistrering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.skoleressurs.SkoleressursService;
import no.fint.graphql.model.model.faggruppe.FaggruppeService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.model.elevfravar.ElevfravarService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsregistreringResource;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevfravarResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelFravarsregistreringResolver")
public class FravarsregistreringResolver implements GraphQLResolver<FravarsregistreringResource> {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private FaggruppeService faggruppeService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private ElevfravarService elevfravarService;


    public CompletionStage<SkoleressursResource> getRegistrertAv(FravarsregistreringResource fravarsregistrering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsregistrering.getRegistrertAv()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FaggruppeResource> getFaggruppe(FravarsregistreringResource fravarsregistrering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsregistrering.getFaggruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> faggruppeService.getFaggruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<UndervisningsgruppeResource> getUndervisningsgruppe(FravarsregistreringResource fravarsregistrering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsregistrering.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

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

