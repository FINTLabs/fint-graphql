
package no.fint.graphql.model.utdanning.vurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningVurderingResolver")
public class VurderingResolver implements GraphQLResolver<VurderingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

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
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<UndervisningsgruppeResource> getUndervisningsgruppe(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(vurdering.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<EksamensgruppeResource> getEksamensgruppe(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(vurdering.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<KarakterverdiResource> getKarakter(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return Flux.fromStream(vurdering.getKarakter()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterverdiService.getKarakterverdiResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

}

