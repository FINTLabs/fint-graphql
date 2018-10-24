// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforholdstype;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.arbeidsforholdstype.ArbeidsforholdstypeService;


import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;


import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArbeidsforholdstypeResolver implements GraphQLResolver<ArbeidsforholdstypeResource> {

	
	@Autowired
	private ArbeidsforholdstypeService arbeidsforholdstypeService;
	
	


	
	
	public ArbeidsforholdstypeResource getArbeidsforholdstype(ArbeidsforholdstypeResource arbeidsforholdstype) {
        return arbeidsforholdstypeService.getArbeidsforholdstypeResource(Links.get(arbeidsforholdstype, "forelder"));
    }
	
	
}

