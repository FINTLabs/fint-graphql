// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.vurdering;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
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


    public ElevforholdResource getElevforhold(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return elevforholdService.getElevforholdResource(
            Links.get(vurdering.getElevforhold()),
            dfe);
    }

    public UndervisningsgruppeResource getUndervisningsgruppe(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(
            Links.get(vurdering.getUndervisningsgruppe()),
            dfe);
    }

    public EksamensgruppeResource getEksamensgruppe(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return eksamensgruppeService.getEksamensgruppeResource(
            Links.get(vurdering.getEksamensgruppe()),
            dfe);
    }

    public KarakterverdiResource getKarakter(VurderingResource vurdering, DataFetchingEnvironment dfe) {
        return karakterverdiService.getKarakterverdiResource(
            Links.get(vurdering.getKarakter()),
            dfe);
    }

}

