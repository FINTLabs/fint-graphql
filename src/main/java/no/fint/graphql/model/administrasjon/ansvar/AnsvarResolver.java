
package no.fint.graphql.model.administrasjon.ansvar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonAnsvarResolver")
public class AnsvarResolver implements GraphQLResolver<AnsvarResource> {

    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private FullmaktService fullmaktService;


    public AnsvarResource getOverordnet(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return ansvar.getOverordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<AnsvarResource> getUnderordnet(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return ansvar.getUnderordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<OrganisasjonselementResource> getOrganisasjonselement(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return ansvar.getOrganisasjonselement()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<FullmaktResource> getFullmakt(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return ansvar.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

