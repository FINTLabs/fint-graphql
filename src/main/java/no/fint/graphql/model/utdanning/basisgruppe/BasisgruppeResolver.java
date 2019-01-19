// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.basisgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
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


    public ArstrinnResource getTrinn(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return arstrinnService.getArstrinnResource(
            Links.get(basisgruppe.getTrinn()),
            dfe);
    }

    public SkoleResource getSkole(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return skoleService.getSkoleResource(
            Links.get(basisgruppe.getSkole()),
            dfe);
    }

    public UndervisningsforholdResource getUndervisningsforhold(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return undervisningsforholdService.getUndervisningsforholdResource(
            Links.get(basisgruppe.getUndervisningsforhold()),
            dfe);
    }

    public ElevforholdResource getElevforhold(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return elevforholdService.getElevforholdResource(
            Links.get(basisgruppe.getElevforhold()),
            dfe);
    }

    public KontaktlarergruppeResource getKontaktlarergruppe(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return kontaktlarergruppeService.getKontaktlarergruppeResource(
            Links.get(basisgruppe.getKontaktlarergruppe()),
            dfe);
    }

    public MedlemskapResource getMedlemskap(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return medlemskapService.getMedlemskapResource(
            Links.get(basisgruppe.getMedlemskap()),
            dfe);
    }

}

