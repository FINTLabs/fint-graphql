// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elevforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.elev.ElevService;
import no.fint.graphql.model.utdanning.elevkategori.ElevkategoriService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.vurdering.VurderingService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.elev.ElevforholdResource;


import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private VurderingService vurderingService;

    @Autowired
    private MedlemskapService medlemskapService;


    public BasisgruppeResource getBasisgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return basisgruppeService.getBasisgruppeResource(
            Links.get(elevforhold.getBasisgruppe()),
            dfe);
    }

    public ElevResource getElev(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevService.getElevResource(
            Links.get(elevforhold.getElev()),
            dfe);
    }

    public ElevkategoriResource getKategori(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return elevkategoriService.getElevkategoriResource(
            Links.get(elevforhold.getKategori()),
            dfe);
    }

    public SkoleResource getSkole(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return skoleService.getSkoleResource(
            Links.get(elevforhold.getSkole()),
            dfe);
    }

    public EksamensgruppeResource getEksamensgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return eksamensgruppeService.getEksamensgruppeResource(
            Links.get(elevforhold.getEksamensgruppe()),
            dfe);
    }

    public KontaktlarergruppeResource getKontaktlarergruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return kontaktlarergruppeService.getKontaktlarergruppeResource(
            Links.get(elevforhold.getKontaktlarergruppe()),
            dfe);
    }

    public UndervisningsgruppeResource getUndervisningsgruppe(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(
            Links.get(elevforhold.getUndervisningsgruppe()),
            dfe);
    }

    public VurderingResource getVurdering(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return vurderingService.getVurderingResource(
            Links.get(elevforhold.getVurdering()),
            dfe);
    }

    public MedlemskapResource getMedlemskap(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return medlemskapService.getMedlemskapResource(
            Links.get(elevforhold.getMedlemskap()),
            dfe);
    }

}

