// Built from tag v3.1.0

package no.fint.graphql.model.felles.person;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.felles.landkode.LandkodeService;
import no.fint.graphql.model.felles.kjonn.KjonnService;
import no.fint.graphql.model.felles.sprak.SprakService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.felles.kontaktperson.KontaktpersonService;
import no.fint.graphql.model.utdanning.elev.ElevService;


import no.fint.model.resource.felles.PersonResource;


import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.model.resource.utdanning.elev.ElevResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonResolver implements GraphQLResolver<PersonResource> {

	
	@Autowired
	private LandkodeService landkodeService;
	
	@Autowired
	private KjonnService kjonnService;
	
	@Autowired
	private SprakService sprakService;
	
	@Autowired
	private PersonalressursService personalressursService;
	
	@Autowired
	private KontaktpersonService kontaktpersonService;
	
	@Autowired
	private ElevService elevService;
	
	


	
	
	public LandkodeResource getLandkode(PersonResource person) {
        return landkodeService.getLandkodeResource(Links.get(person, "statsborgerskap"));
    }
	
	public KjonnResource getKjonn(PersonResource person) {
        return kjonnService.getKjonnResource(Links.get(person, "kjonn"));
    }
	
	public SprakResource getSprak(PersonResource person) {
        return sprakService.getSprakResource(Links.get(person, "malform"));
    }
	
	public PersonalressursResource getPersonalressurs(PersonResource person) {
        return personalressursService.getPersonalressursResource(Links.get(person, "personalressurs"));
    }
	
	public KontaktpersonResource getKontaktperson(PersonResource person) {
        return kontaktpersonService.getKontaktpersonResource(Links.get(person, "parorende"));
    }
	
	public ElevResource getElev(PersonResource person) {
        return elevService.getElevResource(Links.get(person, "elev"));
    }
	
	
}

