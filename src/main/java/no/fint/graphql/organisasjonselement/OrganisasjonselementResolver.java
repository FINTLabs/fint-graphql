package no.fint.graphql.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;
import no.fint.graphql.arbeidsforhold.ArbeidsforholdService;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganisasjonselementResolver implements GraphQLResolver<OrganisasjonselementResource> {

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    public OrganisasjonselementResource getOverordnet(OrganisasjonselementResource organisasjonselement) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(organisasjonselement, "overordnet"));
    }

    public OrganisasjonselementResource getUnderordnet(OrganisasjonselementResource organisasjonselement) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(organisasjonselement, "underordnet"));
    }

    public ArbeidsforholdResource getLeder(OrganisasjonselementResource organisasjonselement) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(organisasjonselement, "leder"));
    }

    public ArbeidsforholdResource getArbeidsforhold(OrganisasjonselementResource organisasjonselement) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(organisasjonselement, "arbeidsforhold"));
    }
}
