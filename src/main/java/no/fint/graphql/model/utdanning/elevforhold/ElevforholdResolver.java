
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
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

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


    public List<BasisgruppeResource> getBasisgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getBasisgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public CompletionStage<ElevResource> getElev(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevService.getElevResource(l, dfe)))
                .flatMap(Mono::flux)
                .filter(Objects::nonNull)
                .singleOrEmpty()
                .toFuture();
    }

    public ElevkategoriResource getKategori(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getKategori()
                .stream()
                .map(Link::getHref)
                .map(l -> elevkategoriService.getElevkategoriResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public SkoleResource getSkole(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<EksamensgruppeResource> getEksamensgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<KontaktlarergruppeResource> getKontaktlarergruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getKontaktlarergruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppeService.getKontaktlarergruppeResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ProgramomradeResource getProgramomrade(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<UndervisningsgruppeResource> getUndervisningsgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<VurderingResource> getVurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getVurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> vurderingService.getVurderingResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<MedlemskapResource> getMedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevforhold.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

