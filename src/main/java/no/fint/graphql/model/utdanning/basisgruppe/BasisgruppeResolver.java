// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.basisgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.arstrinn.ArstrinnService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.elev.BasisgruppeResource;


import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningBasisgruppeResolver")
public class BasisgruppeResolver implements GraphQLResolver<BasisgruppeResource> {


    @Autowired
    private ArstrinnService arstrinnService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public ArstrinnResource getTrinn(BasisgruppeResource basisgruppe) {
        return arstrinnService.getArstrinnResource(Links.get(basisgruppe, "trinn"));
    }

    public SkoleResource getSkole(BasisgruppeResource basisgruppe) {
        return skoleService.getSkoleResource(Links.get(basisgruppe, "skole"));
    }

    public UndervisningsforholdResource getUndervisningsforhold(BasisgruppeResource basisgruppe) {
        return undervisningsforholdService.getUndervisningsforholdResource(Links.get(basisgruppe, "undervisningsforhold"));
    }

    public ElevforholdResource getElevforhold(BasisgruppeResource basisgruppe) {
        return elevforholdService.getElevforholdResource(Links.get(basisgruppe, "elevforhold"));
    }

    public KontaktlarergruppeResource getKontaktlarergruppe(BasisgruppeResource basisgruppe) {
        return kontaktlarergruppeService.getKontaktlarergruppeResource(Links.get(basisgruppe, "kontaktlarergruppe"));
    }

    public MedlemskapResource getMedlemskap(BasisgruppeResource basisgruppe) {
        return medlemskapService.getMedlemskapResource(Links.get(basisgruppe, "medlemskap"));
    }

}

