
package no.fint.graphql.model.administrasjon.organisasjonselement;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonOrganisasjonselementService")
public class OrganisasjonselementService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public OrganisasjonselementResource getOrganisasjonselementResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getOrganisasjonselementResource(
            endpoints.getAdministrasjonOrganisasjon() 
                + "/organisasjonselement/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public OrganisasjonselementResource getOrganisasjonselementResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, OrganisasjonselementResource.class, dfe);
    }
}

