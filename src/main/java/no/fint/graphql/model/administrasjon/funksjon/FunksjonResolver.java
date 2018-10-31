// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.funksjon;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;


import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFunksjonResolver")
public class FunksjonResolver implements GraphQLResolver<FunksjonResource> {


    @Autowired
    private FunksjonService funksjonService;

    @Autowired
    private FullmaktService fullmaktService;


    public FunksjonResource getOverordnet(FunksjonResource funksjon) {
        return funksjonService.getFunksjonResource(Links.get(funksjon.getOverordnet()));
    }

    public FunksjonResource getUnderordnet(FunksjonResource funksjon) {
        return funksjonService.getFunksjonResource(Links.get(funksjon.getUnderordnet()));
    }

    public FullmaktResource getFullmakt(FunksjonResource funksjon) {
        return fullmaktService.getFullmaktResource(Links.get(funksjon.getFullmakt()));
    }

}

