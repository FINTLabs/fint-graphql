// Built from tag master

package no.fint.graphql.model.administrasjon.fravar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.fravarsgrunn.FravarsgrunnService;
import no.fint.graphql.model.administrasjon.fravarstype.FravarstypeService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.administrasjon.fravar.FravarService;


import no.fint.model.resource.administrasjon.personal.FravarResource;


import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.FravarResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFravarResolver")
public class FravarResolver implements GraphQLResolver<FravarResource> {


    @Autowired
    private FravarsgrunnService fravarsgrunnService;

    @Autowired
    private FravarstypeService fravarstypeService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private FravarService fravarService;


    public FravarsgrunnResource getFravarsgrunn(FravarResource fravar) {
        return fravarsgrunnService.getFravarsgrunnResource(Links.get(fravar.getFravarsgrunn()));
    }

    public FravarstypeResource getFravarstype(FravarResource fravar) {
        return fravarstypeService.getFravarstypeResource(Links.get(fravar.getFravarstype()));
    }

    public ArbeidsforholdResource getArbeidsforhold(FravarResource fravar) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(fravar.getArbeidsforhold()));
    }

    public FravarResource getFortsettelse(FravarResource fravar) {
        return fravarService.getFravarResource(Links.get(fravar.getFortsettelse()));
    }

    public FravarResource getFortsetter(FravarResource fravar) {
        return fravarService.getFravarResource(Links.get(fravar.getFortsetter()));
    }

}

