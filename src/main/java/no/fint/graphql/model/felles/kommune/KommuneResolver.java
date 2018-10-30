// Built from tag master

package no.fint.graphql.model.felles.kommune;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.felles.fylke.FylkeService;


import no.fint.model.resource.felles.kodeverk.KommuneResource;


import no.fint.model.resource.felles.kodeverk.FylkeResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesKommuneResolver")
public class KommuneResolver implements GraphQLResolver<KommuneResource> {


    @Autowired
    private FylkeService fylkeService;


    public FylkeResource getFylke(KommuneResource kommune) {
        return fylkeService.getFylkeResource(Links.get(kommune.getFylke()));
    }

}

