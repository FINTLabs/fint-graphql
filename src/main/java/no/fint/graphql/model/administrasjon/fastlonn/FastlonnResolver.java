// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fastlonn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;


import no.fint.model.resource.administrasjon.personal.FastlonnResource;


import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FastlonnResolver implements GraphQLResolver<FastlonnResource> {

	
	@Autowired
	private LonnsartService lonnsartService;
	
	@Autowired
	private PersonalressursService personalressursService;
	
	@Autowired
	private ArbeidsforholdService arbeidsforholdService;
	
	


	
	
	public LonnsartResource getLonnsart(FastlonnResource fastlonn) {
        return lonnsartService.getLonnsartResource(Links.get(fastlonn, "lonnsart"));
    }
	
	public PersonalressursResource getPersonalressurs(FastlonnResource fastlonn) {
        return personalressursService.getPersonalressursResource(Links.get(fastlonn, "anviser"));
    }
	
	public ArbeidsforholdResource getArbeidsforhold(FastlonnResource fastlonn) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(fastlonn, "arbeidsforhold"));
    }
	
	
}

