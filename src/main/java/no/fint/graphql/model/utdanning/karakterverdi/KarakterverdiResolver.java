// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterverdi;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.karakterskala.KarakterskalaService;


import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;


import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningKarakterverdiResolver")
public class KarakterverdiResolver implements GraphQLResolver<KarakterverdiResource> {

	
	@Autowired
	private KarakterskalaService karakterskalaService;
	
	


	
	
	public KarakterskalaResource getKarakterskala(KarakterverdiResource karakterverdi) {
        return karakterskalaService.getKarakterskalaResource(Links.get(karakterverdi, "skala"));
    }
	
	
}

