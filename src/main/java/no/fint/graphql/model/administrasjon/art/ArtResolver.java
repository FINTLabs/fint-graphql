// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.art;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.administrasjon.kodeverk.ArtResource;


import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonArtResolver")
public class ArtResolver implements GraphQLResolver<ArtResource> {


    @Autowired
    private FullmaktService fullmaktService;


    public FullmaktResource getFullmakt(ArtResource art) {
        return fullmaktService.getFullmaktResource(Links.get(art.getFullmakt()));
    }

}

