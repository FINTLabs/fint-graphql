
package no.fint.graphql.model.model.vurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.model.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.model.karakterverdi.KarakterverdiService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.KarakterverdiResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelVurderingResolver")
public class VurderingResolver implements GraphQLResolver<VurderingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private FagService fagService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private KarakterverdiService karakterverdiService;


    public CompletionStage<ElevforholdResource> getElevforhold(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(vurdering.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FagResource> getFag(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(vurdering.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<UndervisningsgruppeResource> getUndervisningsgruppe(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(vurdering.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<EksamensgruppeResource> getEksamensgruppe(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(vurdering.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getKarakter(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(vurdering.getKarakter()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

