// Built from tag master

package no.fint.graphql.model.administrasjon.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;


import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;


import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonOrganisasjonselementResolver")
public class OrganisasjonselementResolver implements GraphQLResolver<OrganisasjonselementResource> {


    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;


    public AnsvarResource getAnsvar(OrganisasjonselementResource organisasjonselement) {
        return ansvarService.getAnsvarResource(Links.get(organisasjonselement.getAnsvar()));
    }

    public PersonalressursResource getLeder(OrganisasjonselementResource organisasjonselement) {
        return personalressursService.getPersonalressursResource(Links.get(organisasjonselement.getLeder()));
    }

    public OrganisasjonselementResource getOverordnet(OrganisasjonselementResource organisasjonselement) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(organisasjonselement.getOverordnet()));
    }

    public OrganisasjonselementResource getUnderordnet(OrganisasjonselementResource organisasjonselement) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(organisasjonselement.getUnderordnet()));
    }

    public SkoleResource getSkole(OrganisasjonselementResource organisasjonselement) {
        return skoleService.getSkoleResource(Links.get(organisasjonselement.getSkole()));
    }

    public ArbeidsforholdResource getArbeidsforhold(OrganisasjonselementResource organisasjonselement) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(organisasjonselement.getArbeidsforhold()));
    }

}

