// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterskala;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.karakterverdi.KarakterverdiService;


import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;


import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KarakterskalaResolver implements GraphQLResolver<KarakterskalaResource> {

	
	@Autowired
	private KarakterverdiService karakterverdiService;
	
	


	
	
	public KarakterverdiResource getKarakterverdi(KarakterskalaResource karakterskala) {
        return karakterverdiService.getKarakterverdiResource(Links.get(karakterskala, "verdi"));
    }
	
	
}

