
package no.fint.graphql.model.utdanning.elevforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.elev.ElevService;
import no.fint.graphql.model.utdanning.elevkategori.ElevkategoriService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.vurdering.VurderingService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningElevforholdResolver")
public class ElevforholdResolver implements GraphQLResolver<ElevforholdResource> {

    @Autowired
    private BasisgruppeService basisgruppeService;

    @Autowired
    private ElevService elevService;

    @Autowired
    private ElevkategoriService elevkategoriService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private VurderingService vurderingService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<List<BasisgruppeResource>> getBasisgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getBasisgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<ElevResource> getElev(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevService.getElevResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ElevkategoriResource> getKategori(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getKategori()
                .stream()
                .map(Link::getHref)
                .map(l -> elevkategoriService.getElevkategoriResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppeResource>> getEksamensgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KontaktlarergruppeResource>> getKontaktlarergruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getKontaktlarergruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppeService.getKontaktlarergruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<ProgramomradeResource> getProgramomrade(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsgruppeResource>> getUndervisningsgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<VurderingResource>> getVurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getVurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> vurderingService.getVurderingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

