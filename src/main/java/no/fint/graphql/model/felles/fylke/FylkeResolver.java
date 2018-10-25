// Built from tag v3.1.0

package no.fint.graphql.model.felles.fylke;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.felles.kommune.KommuneService;


import no.fint.model.resource.felles.kodeverk.FylkeResource;


import no.fint.model.resource.felles.kodeverk.KommuneResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("fellesFylkeResolver")
public class FylkeResolver implements GraphQLResolver<FylkeResource> {

	
	@Autowired
	private KommuneService kommuneService;
	
	


	
	
	public KommuneResource getKommune(FylkeResource fylke) {
        return kommuneService.getKommuneResource(Links.get(fylke, "kommune"));
    }
	
	
}

