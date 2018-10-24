// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.stillingskode;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.stillingskode.StillingskodeService;


import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;


import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StillingskodeResolver implements GraphQLResolver<StillingskodeResource> {

	
	@Autowired
	private StillingskodeService stillingskodeService;
	
	


	
	
	public StillingskodeResource getStillingskode(StillingskodeResource stillingskode) {
        return stillingskodeService.getStillingskodeResource(Links.get(stillingskode, "forelder"));
    }
	
	
}

