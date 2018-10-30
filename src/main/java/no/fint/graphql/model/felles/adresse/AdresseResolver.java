// Built from tag master

package no.fint.graphql.model.felles.adresse;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.felles.landkode.LandkodeService;


import no.fint.model.resource.felles.kompleksedatatyper.AdresseResource;


import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesAdresseResolver")
public class AdresseResolver implements GraphQLResolver<AdresseResource> {


    @Autowired
    private LandkodeService landkodeService;


    public LandkodeResource getLand(AdresseResource adresse) {
        return landkodeService.getLandkodeResource(Links.get(adresse.getLand()));
    }

}

