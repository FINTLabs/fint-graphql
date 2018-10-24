// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;


import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KontaktlarergruppeResolver implements GraphQLResolver<KontaktlarergruppeResource> {

	
	@Autowired
	private BasisgruppeService basisgruppeService;
	
	@Autowired
	private SkoleService skoleService;
	
	@Autowired
	private ElevforholdService elevforholdService;
	
	@Autowired
	private UndervisningsforholdService undervisningsforholdService;
	
	@Autowired
	private MedlemskapService medlemskapService;
	
	


	
	
	public BasisgruppeResource getBasisgruppe(KontaktlarergruppeResource kontaktlarergruppe) {
        return basisgruppeService.getBasisgruppeResource(Links.get(kontaktlarergruppe, "basisgruppe"));
    }
	
	public SkoleResource getSkole(KontaktlarergruppeResource kontaktlarergruppe) {
        return skoleService.getSkoleResource(Links.get(kontaktlarergruppe, "skole"));
    }
	
	public ElevforholdResource getElevforhold(KontaktlarergruppeResource kontaktlarergruppe) {
        return elevforholdService.getElevforholdResource(Links.get(kontaktlarergruppe, "elevforhold"));
    }
	
	public UndervisningsforholdResource getUndervisningsforhold(KontaktlarergruppeResource kontaktlarergruppe) {
        return undervisningsforholdService.getUndervisningsforholdResource(Links.get(kontaktlarergruppe, "undervisningsforhold"));
    }
	
	public MedlemskapResource getMedlemskap(KontaktlarergruppeResource kontaktlarergruppe) {
        return medlemskapService.getMedlemskapResource(Links.get(kontaktlarergruppe, "medlemskap"));
    }
	
	
}

