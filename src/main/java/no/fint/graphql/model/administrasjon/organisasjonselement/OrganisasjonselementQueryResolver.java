// Built from tag master

package no.fint.graphql.model.administrasjon.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonOrganisasjonselementQueryResolver")
public class OrganisasjonselementQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OrganisasjonselementService service;

    public List<OrganisasjonselementResource> getOrganisasjonselement(String sinceTimeStamp) {
        OrganisasjonselementResources resources = service.getOrganisasjonselementResources(sinceTimeStamp);
        return resources.getContent();
    }
}
