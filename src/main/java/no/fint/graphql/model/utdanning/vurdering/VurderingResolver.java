// Built from tag master

package no.fint.graphql.model.utdanning.vurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;


import no.fint.model.resource.utdanning.vurdering.VurderingResource;


import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    public ElevforholdResource getElevforhold(VurderingResource vurdering) {
        return elevforholdService.getElevforholdResource(Links.get(vurdering.getElevforhold()));
    }

    public UndervisningsgruppeResource getUndervisningsgruppe(VurderingResource vurdering) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(Links.get(vurdering.getUndervisningsgruppe()));
    }

    public EksamensgruppeResource getEksamensgruppe(VurderingResource vurdering) {
        return eksamensgruppeService.getEksamensgruppeResource(Links.get(vurdering.getEksamensgruppe()));
    }

    public KarakterverdiResource getKarakter(VurderingResource vurdering) {
        return karakterverdiService.getKarakterverdiResource(Links.get(vurdering.getKarakter()));
    }

}

