// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.eksamensgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;


import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningEksamensgruppeResolver")
public class EksamensgruppeResolver implements GraphQLResolver<EksamensgruppeResource> {


    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private MedlemskapService medlemskapService;


    public FagResource getFag(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return fagService.getFagResource(
            Links.get(eksamensgruppe.getFag()),
            dfe);
    }

    public SkoleResource getSkole(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return skoleService.getSkoleResource(
            Links.get(eksamensgruppe.getSkole()),
            dfe);
    }

    public ElevforholdResource getElevforhold(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return elevforholdService.getElevforholdResource(
            Links.get(eksamensgruppe.getElevforhold()),
            dfe);
    }

    public UndervisningsforholdResource getUndervisningsforhold(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return undervisningsforholdService.getUndervisningsforholdResource(
            Links.get(eksamensgruppe.getUndervisningsforhold()),
            dfe);
    }

    public MedlemskapResource getMedlemskap(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return medlemskapService.getMedlemskapResource(
            Links.get(eksamensgruppe.getMedlemskap()),
            dfe);
    }

}

