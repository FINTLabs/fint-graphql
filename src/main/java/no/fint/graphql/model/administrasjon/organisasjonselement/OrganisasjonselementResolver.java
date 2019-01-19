// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
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


    public AnsvarResource getAnsvar(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return ansvarService.getAnsvarResource(
            Links.get(organisasjonselement.getAnsvar()),
            dfe);
    }

    public PersonalressursResource getLeder(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return personalressursService.getPersonalressursResource(
            Links.get(organisasjonselement.getLeder()),
            dfe);
    }

    public OrganisasjonselementResource getOverordnet(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return organisasjonselementService.getOrganisasjonselementResource(
            Links.get(organisasjonselement.getOverordnet()),
            dfe);
    }

    public OrganisasjonselementResource getUnderordnet(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return organisasjonselementService.getOrganisasjonselementResource(
            Links.get(organisasjonselement.getUnderordnet()),
            dfe);
    }

    public SkoleResource getSkole(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return skoleService.getSkoleResource(
            Links.get(organisasjonselement.getSkole()),
            dfe);
    }

    public ArbeidsforholdResource getArbeidsforhold(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return arbeidsforholdService.getArbeidsforholdResource(
            Links.get(organisasjonselement.getArbeidsforhold()),
            dfe);
    }

}

