
package no.fint.graphql.model.administrasjon.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


    public List<AnsvarResource> getAnsvar(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return organisasjonselement.getAnsvar()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PersonalressursResource getLeder(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return organisasjonselement.getLeder()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public OrganisasjonselementResource getOverordnet(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return organisasjonselement.getOverordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<OrganisasjonselementResource> getUnderordnet(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return organisasjonselement.getUnderordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public SkoleResource getSkole(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return organisasjonselement.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<ArbeidsforholdResource> getArbeidsforhold(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return organisasjonselement.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

