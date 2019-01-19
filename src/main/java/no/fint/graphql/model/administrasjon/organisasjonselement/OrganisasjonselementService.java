// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.organisasjonselement;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonOrganisasjonselementService")
public class OrganisasjonselementService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public OrganisasjonselementResources getOrganisasjonselementResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonOrganisasjon() + "/organisasjonselement",
                    sinceTimeStamp),
                OrganisasjonselementResources.class,
                dfe);
    }

    public OrganisasjonselementResource getOrganisasjonselementResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, OrganisasjonselementResource.class, dfe);
    }
}

