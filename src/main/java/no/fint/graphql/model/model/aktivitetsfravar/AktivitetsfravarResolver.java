
package no.fint.graphql.model.model.aktivitetsfravar;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevfravar.ElevfravarService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.skoleressurs.SkoleressursService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.AktivitetsfravarResource;
import no.novari.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAktivitetsfravarResolver")
public class AktivitetsfravarResolver {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private FagService fagService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private ElevfravarService elevfravarService;


    @SchemaMapping(typeName = "Aktivitetsfravar", field = "registrertAv")
    public CompletionStage<SkoleressursResource> getRegistrertAv(AktivitetsfravarResource aktivitetsfravar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(aktivitetsfravar.getRegistrertAv()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Aktivitetsfravar", field = "fag")
    public CompletionStage<FagResource> getFag(AktivitetsfravarResource aktivitetsfravar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(aktivitetsfravar.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Aktivitetsfravar", field = "undervisningsgruppe")
    public CompletionStage<UndervisningsgruppeResource> getUndervisningsgruppe(AktivitetsfravarResource aktivitetsfravar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(aktivitetsfravar.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Aktivitetsfravar", field = "elevfravar")
    public CompletionStage<ElevfravarResource> getElevfravar(AktivitetsfravarResource aktivitetsfravar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(aktivitetsfravar.getElevfravar()
                .stream()
                .map(Link::getHref)
                .map(l -> elevfravarService.getElevfravarResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

