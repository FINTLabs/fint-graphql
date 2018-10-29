// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elevforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.elev.ElevService;
import no.fint.graphql.model.utdanning.elevkategori.ElevkategoriService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.vurdering.VurderingService;
import no.fint.graphql.model.utdanning.fravar.FravarService;
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
import no.fint.model.resource.utdanning.vurdering.FravarResource;
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
    private FravarService fravarService;

    @Autowired
    private MedlemskapService medlemskapService;


    public BasisgruppeResource getBasisgruppe(ElevforholdResource elevforhold) {
        return basisgruppeService.getBasisgruppeResource(Links.get(elevforhold, "basisgruppe"));
    }

    public ElevResource getElev(ElevforholdResource elevforhold) {
        return elevService.getElevResource(Links.get(elevforhold, "elev"));
    }

    public ElevkategoriResource getKategori(ElevforholdResource elevforhold) {
        return elevkategoriService.getElevkategoriResource(Links.get(elevforhold, "kategori"));
    }

    public SkoleResource getSkole(ElevforholdResource elevforhold) {
        return skoleService.getSkoleResource(Links.get(elevforhold, "skole"));
    }

    public EksamensgruppeResource getEksamensgruppe(ElevforholdResource elevforhold) {
        return eksamensgruppeService.getEksamensgruppeResource(Links.get(elevforhold, "eksamensgruppe"));
    }

    public KontaktlarergruppeResource getKontaktlarergruppe(ElevforholdResource elevforhold) {
        return kontaktlarergruppeService.getKontaktlarergruppeResource(Links.get(elevforhold, "kontaktlarergruppe"));
    }

    public UndervisningsgruppeResource getUndervisningsgruppe(ElevforholdResource elevforhold) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(Links.get(elevforhold, "undervisningsgruppe"));
    }

    public VurderingResource getVurdering(ElevforholdResource elevforhold) {
        return vurderingService.getVurderingResource(Links.get(elevforhold, "vurdering"));
    }

    public FravarResource getFravar(ElevforholdResource elevforhold) {
        return fravarService.getFravarResource(Links.get(elevforhold, "fravar"));
    }

    public MedlemskapResource getMedlemskap(ElevforholdResource elevforhold) {
        return medlemskapService.getMedlemskapResource(Links.get(elevforhold, "medlemskap"));
    }

}

