// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.funksjon;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
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


    public FunksjonResource getOverordnet(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return funksjonService.getFunksjonResource(
            Links.get(funksjon.getOverordnet()),
            dfe);
    }

    public FunksjonResource getUnderordnet(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return funksjonService.getFunksjonResource(
            Links.get(funksjon.getUnderordnet()),
            dfe);
    }

    public FullmaktResource getFullmakt(FunksjonResource funksjon, DataFetchingEnvironment dfe) {
        return fullmaktService.getFullmaktResource(
            Links.get(funksjon.getFullmakt()),
            dfe);
    }

}

