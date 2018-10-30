// Built from tag master

package no.fint.graphql.model.administrasjon.prosjekt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;


import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonProsjektResolver")
public class ProsjektResolver implements GraphQLResolver<ProsjektResource> {


    @Autowired
    private FullmaktService fullmaktService;


    public FullmaktResource getFullmakt(ProsjektResource prosjekt) {
        return fullmaktService.getFullmaktResource(Links.get(prosjekt.getFullmakt()));
    }

}

