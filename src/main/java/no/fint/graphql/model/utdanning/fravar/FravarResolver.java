// Built from tag master

package no.fint.graphql.model.utdanning.fravar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.fravarstype.FravarstypeService;


import no.fint.model.resource.utdanning.vurdering.FravarResource;


import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningFravarResolver")
public class FravarResolver implements GraphQLResolver<FravarResource> {


    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private FravarstypeService fravarstypeService;


    public ElevforholdResource getElevforhold(FravarResource fravar) {
        return elevforholdService.getElevforholdResource(Links.get(fravar.getElevforhold()));
    }

    public UndervisningsgruppeResource getUndervisningsgruppe(FravarResource fravar) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(Links.get(fravar.getUndervisningsgruppe()));
    }

    public EksamensgruppeResource getEksamensgruppe(FravarResource fravar) {
        return eksamensgruppeService.getEksamensgruppeResource(Links.get(fravar.getEksamensgruppe()));
    }

    public FravarstypeResource getFravarstype(FravarResource fravar) {
        return fravarstypeService.getFravarstypeResource(Links.get(fravar.getFravarstype()));
    }

}

