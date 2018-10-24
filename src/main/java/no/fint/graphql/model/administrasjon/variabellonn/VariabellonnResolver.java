// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.variabellonn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.lonnsart.LonnsartService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;


import no.fint.model.resource.administrasjon.personal.VariabellonnResource;


import no.fint.model.resource.administrasjon.kodeverk.LonnsartResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VariabellonnResolver implements GraphQLResolver<VariabellonnResource> {

	
	@Autowired
	private LonnsartService lonnsartService;
	
	@Autowired
	private PersonalressursService personalressursService;
	
	@Autowired
	private ArbeidsforholdService arbeidsforholdService;
	
	


	
	
	public LonnsartResource getLonnsart(VariabellonnResource variabellonn) {
        return lonnsartService.getLonnsartResource(Links.get(variabellonn, "lonnsart"));
    }
	
	public PersonalressursResource getPersonalressurs(VariabellonnResource variabellonn) {
        return personalressursService.getPersonalressursResource(Links.get(variabellonn, "anviser"));
    }
	
	public ArbeidsforholdResource getArbeidsforhold(VariabellonnResource variabellonn) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(variabellonn, "arbeidsforhold"));
    }
	
	
}

