// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.eksamensgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;


import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EksamensgruppeResolver implements GraphQLResolver<EksamensgruppeResource> {

	
	@Autowired
	private FagService fagService;
	
	@Autowired
	private SkoleService skoleService;
	
	@Autowired
	private ElevforholdService elevforholdService;
	
	@Autowired
	private UndervisningsforholdService undervisningsforholdService;
	
	@Autowired
	private MedlemskapService medlemskapService;
	
	


	
	
	public FagResource getFag(EksamensgruppeResource eksamensgruppe) {
        return fagService.getFagResource(Links.get(eksamensgruppe, "fag"));
    }
	
	public SkoleResource getSkole(EksamensgruppeResource eksamensgruppe) {
        return skoleService.getSkoleResource(Links.get(eksamensgruppe, "skole"));
    }
	
	public ElevforholdResource getElevforhold(EksamensgruppeResource eksamensgruppe) {
        return elevforholdService.getElevforholdResource(Links.get(eksamensgruppe, "elevforhold"));
    }
	
	public UndervisningsforholdResource getUndervisningsforhold(EksamensgruppeResource eksamensgruppe) {
        return undervisningsforholdService.getUndervisningsforholdResource(Links.get(eksamensgruppe, "undervisningsforhold"));
    }
	
	public MedlemskapResource getMedlemskap(EksamensgruppeResource eksamensgruppe) {
        return medlemskapService.getMedlemskapResource(Links.get(eksamensgruppe, "medlemskap"));
    }
	
	
}

