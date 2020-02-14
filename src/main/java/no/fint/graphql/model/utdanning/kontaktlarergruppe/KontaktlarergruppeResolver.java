
package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningKontaktlarergruppeResolver")
public class KontaktlarergruppeResolver implements GraphQLResolver<KontaktlarergruppeResource> {

    @Autowired
    private BasisgruppeService basisgruppeService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<List<BasisgruppeResource>> getBasisgruppe(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getBasisgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<ElevforholdResource>> getElevforhold(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppe.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

