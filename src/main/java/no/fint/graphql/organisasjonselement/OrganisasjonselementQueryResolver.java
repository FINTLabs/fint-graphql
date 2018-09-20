package no.fint.graphql.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganisasjonselementQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    public List<OrganisasjonselementResource> getOrganisasjonselement(String sinceTimeStamp) {
        OrganisasjonselementResources resources = organisasjonselementService.getOrganisasjonselementResources(sinceTimeStamp);
        return resources.getContent();
    }
}
