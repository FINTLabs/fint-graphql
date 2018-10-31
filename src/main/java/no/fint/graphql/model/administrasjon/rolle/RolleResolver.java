// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.rolle;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.administrasjon.fullmakt.RolleResource;


import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonRolleResolver")
public class RolleResolver implements GraphQLResolver<RolleResource> {


    @Autowired
    private FullmaktService fullmaktService;


    public FullmaktResource getFullmakt(RolleResource rolle) {
        return fullmaktService.getFullmaktResource(Links.get(rolle.getFullmakt()));
    }

}

