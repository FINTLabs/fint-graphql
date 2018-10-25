// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skole;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skoleeiertype.SkoleeiertypeService;
import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.utdanningsprogram.UtdanningsprogramService;


import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;


import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningSkoleResolver")
public class SkoleResolver implements GraphQLResolver<SkoleResource> {

	
	@Autowired
	private OrganisasjonselementService organisasjonselementService;
	
	@Autowired
	private FagService fagService;
	
	@Autowired
	private SkoleeiertypeService skoleeiertypeService;
	
	@Autowired
	private BasisgruppeService basisgruppeService;
	
	@Autowired
	private ElevforholdService elevforholdService;
	
	@Autowired
	private KontaktlarergruppeService kontaktlarergruppeService;
	
	@Autowired
	private SkoleressursService skoleressursService;
	
	@Autowired
	private UndervisningsforholdService undervisningsforholdService;
	
	@Autowired
	private UndervisningsgruppeService undervisningsgruppeService;
	
	@Autowired
	private EksamensgruppeService eksamensgruppeService;
	
	@Autowired
	private UtdanningsprogramService utdanningsprogramService;
	
	


	
	
	public OrganisasjonselementResource getOrganisasjonselement(SkoleResource skole) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(skole, "organisasjon"));
    }
	
	public FagResource getFag(SkoleResource skole) {
        return fagService.getFagResource(Links.get(skole, "fag"));
    }
	
	public SkoleeiertypeResource getSkoleeiertype(SkoleResource skole) {
        return skoleeiertypeService.getSkoleeiertypeResource(Links.get(skole, "skoleeierType"));
    }
	
	public BasisgruppeResource getBasisgruppe(SkoleResource skole) {
        return basisgruppeService.getBasisgruppeResource(Links.get(skole, "basisgruppe"));
    }
	
	public ElevforholdResource getElevforhold(SkoleResource skole) {
        return elevforholdService.getElevforholdResource(Links.get(skole, "elevforhold"));
    }
	
	public KontaktlarergruppeResource getKontaktlarergruppe(SkoleResource skole) {
        return kontaktlarergruppeService.getKontaktlarergruppeResource(Links.get(skole, "kontaktlarergruppe"));
    }
	
	public SkoleressursResource getSkoleressurs(SkoleResource skole) {
        return skoleressursService.getSkoleressursResource(Links.get(skole, "skoleressurs"));
    }
	
	public UndervisningsforholdResource getUndervisningsforhold(SkoleResource skole) {
        return undervisningsforholdService.getUndervisningsforholdResource(Links.get(skole, "undervisningsforhold"));
    }
	
	public UndervisningsgruppeResource getUndervisningsgruppe(SkoleResource skole) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(Links.get(skole, "undervisningsgruppe"));
    }
	
	public EksamensgruppeResource getEksamensgruppe(SkoleResource skole) {
        return eksamensgruppeService.getEksamensgruppeResource(Links.get(skole, "eksamensgruppe"));
    }
	
	public UtdanningsprogramResource getUtdanningsprogram(SkoleResource skole) {
        return utdanningsprogramService.getUtdanningsprogramResource(Links.get(skole, "utdanningsprogram"));
    }
	
	
}

