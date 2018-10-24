// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.arbeidsforholdstype.ArbeidsforholdstypeService;
import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.stillingskode.StillingskodeService;
import no.fint.graphql.model.administrasjon.uketimetall.UketimetallService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.fravar.FravarService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;


import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;


import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.FravarResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArbeidsforholdResolver implements GraphQLResolver<ArbeidsforholdResource> {

	
	@Autowired
	private AnsvarService ansvarService;
	
	@Autowired
	private ArbeidsforholdstypeService arbeidsforholdstypeService;
	
	@Autowired
	private FunksjonService funksjonService;
	
	@Autowired
	private StillingskodeService stillingskodeService;
	
	@Autowired
	private UketimetallService uketimetallService;
	
	@Autowired
	private OrganisasjonselementService organisasjonselementService;
	
	@Autowired
	private PersonalressursService personalressursService;
	
	@Autowired
	private FravarService fravarService;
	
	@Autowired
	private UndervisningsforholdService undervisningsforholdService;
	
	


	
	
	public AnsvarResource getAnsvar(ArbeidsforholdResource arbeidsforhold) {
        return ansvarService.getAnsvarResource(Links.get(arbeidsforhold, "ansvar"));
    }
	
	public ArbeidsforholdstypeResource getArbeidsforholdstype(ArbeidsforholdResource arbeidsforhold) {
        return arbeidsforholdstypeService.getArbeidsforholdstypeResource(Links.get(arbeidsforhold, "arbeidsforholdstype"));
    }
	
	public FunksjonResource getFunksjon(ArbeidsforholdResource arbeidsforhold) {
        return funksjonService.getFunksjonResource(Links.get(arbeidsforhold, "funksjon"));
    }
	
	public StillingskodeResource getStillingskode(ArbeidsforholdResource arbeidsforhold) {
        return stillingskodeService.getStillingskodeResource(Links.get(arbeidsforhold, "stillingskode"));
    }
	
	public UketimetallResource getUketimetall(ArbeidsforholdResource arbeidsforhold) {
        return uketimetallService.getUketimetallResource(Links.get(arbeidsforhold, "timerPerUke"));
    }
	
	public OrganisasjonselementResource getOrganisasjonselement(ArbeidsforholdResource arbeidsforhold) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(arbeidsforhold, "arbeidssted"));
    }
	
	public PersonalressursResource getPersonalressurs(ArbeidsforholdResource arbeidsforhold) {
        return personalressursService.getPersonalressursResource(Links.get(arbeidsforhold, "personalleder"));
    }
	
	public FravarResource getFravar(ArbeidsforholdResource arbeidsforhold) {
        return fravarService.getFravarResource(Links.get(arbeidsforhold, "fravar"));
    }
	
	public UndervisningsforholdResource getUndervisningsforhold(ArbeidsforholdResource arbeidsforhold) {
        return undervisningsforholdService.getUndervisningsforholdResource(Links.get(arbeidsforhold, "undervisningsforhold"));
    }
	
	
}

