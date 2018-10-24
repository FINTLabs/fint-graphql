// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.time.TimeService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;


import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UndervisningsforholdResolver implements GraphQLResolver<UndervisningsforholdResource> {

	
	@Autowired
	private ArbeidsforholdService arbeidsforholdService;
	
	@Autowired
	private BasisgruppeService basisgruppeService;
	
	@Autowired
	private KontaktlarergruppeService kontaktlarergruppeService;
	
	@Autowired
	private UndervisningsgruppeService undervisningsgruppeService;
	
	@Autowired
	private EksamensgruppeService eksamensgruppeService;
	
	@Autowired
	private TimeService timeService;
	
	@Autowired
	private SkoleService skoleService;
	
	@Autowired
	private SkoleressursService skoleressursService;
	
	@Autowired
	private MedlemskapService medlemskapService;
	
	


	
	
	public ArbeidsforholdResource getArbeidsforhold(UndervisningsforholdResource undervisningsforhold) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(undervisningsforhold, "arbeidsforhold"));
    }
	
	public BasisgruppeResource getBasisgruppe(UndervisningsforholdResource undervisningsforhold) {
        return basisgruppeService.getBasisgruppeResource(Links.get(undervisningsforhold, "basisgruppe"));
    }
	
	public KontaktlarergruppeResource getKontaktlarergruppe(UndervisningsforholdResource undervisningsforhold) {
        return kontaktlarergruppeService.getKontaktlarergruppeResource(Links.get(undervisningsforhold, "kontaktlarergruppe"));
    }
	
	public UndervisningsgruppeResource getUndervisningsgruppe(UndervisningsforholdResource undervisningsforhold) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(Links.get(undervisningsforhold, "undervisningsgruppe"));
    }
	
	public EksamensgruppeResource getEksamensgruppe(UndervisningsforholdResource undervisningsforhold) {
        return eksamensgruppeService.getEksamensgruppeResource(Links.get(undervisningsforhold, "eksamensgruppe"));
    }
	
	public TimeResource getTime(UndervisningsforholdResource undervisningsforhold) {
        return timeService.getTimeResource(Links.get(undervisningsforhold, "time"));
    }
	
	public SkoleResource getSkole(UndervisningsforholdResource undervisningsforhold) {
        return skoleService.getSkoleResource(Links.get(undervisningsforhold, "skole"));
    }
	
	public SkoleressursResource getSkoleressurs(UndervisningsforholdResource undervisningsforhold) {
        return skoleressursService.getSkoleressursResource(Links.get(undervisningsforhold, "skoleressurs"));
    }
	
	public MedlemskapResource getMedlemskap(UndervisningsforholdResource undervisningsforhold) {
        return medlemskapService.getMedlemskapResource(Links.get(undervisningsforhold, "medlemskap"));
    }
	
	
}

