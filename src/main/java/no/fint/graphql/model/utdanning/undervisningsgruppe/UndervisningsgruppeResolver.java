// Built from tag master

package no.fint.graphql.model.utdanning.undervisningsgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.time.TimeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;


import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningUndervisningsgruppeResolver")
public class UndervisningsgruppeResolver implements GraphQLResolver<UndervisningsgruppeResource> {


    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public FagResource getFag(UndervisningsgruppeResource undervisningsgruppe) {
        return fagService.getFagResource(Links.get(undervisningsgruppe.getFag()));
    }

    public SkoleResource getSkole(UndervisningsgruppeResource undervisningsgruppe) {
        return skoleService.getSkoleResource(Links.get(undervisningsgruppe.getSkole()));
    }

    public ElevforholdResource getElevforhold(UndervisningsgruppeResource undervisningsgruppe) {
        return elevforholdService.getElevforholdResource(Links.get(undervisningsgruppe.getElevforhold()));
    }

    public UndervisningsforholdResource getUndervisningsforhold(UndervisningsgruppeResource undervisningsgruppe) {
        return undervisningsforholdService.getUndervisningsforholdResource(Links.get(undervisningsgruppe.getUndervisningsforhold()));
    }

    public TimeResource getTime(UndervisningsgruppeResource undervisningsgruppe) {
        return timeService.getTimeResource(Links.get(undervisningsgruppe.getTime()));
    }

    public MedlemskapResource getMedlemskap(UndervisningsgruppeResource undervisningsgruppe) {
        return medlemskapService.getMedlemskapResource(Links.get(undervisningsgruppe.getMedlemskap()));
    }

}

