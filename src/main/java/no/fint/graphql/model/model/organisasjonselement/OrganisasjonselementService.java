
package no.fint.graphql.model.model.organisasjonselement;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelOrganisasjonselementService")
public class OrganisasjonselementService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<OrganisasjonselementResource> getOrganisasjonselementResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getOrganisasjonselementResource(
            endpoints.getAdministrasjonOrganisasjon() 
                + "/organisasjonselement/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<OrganisasjonselementResource> getOrganisasjonselementResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, OrganisasjonselementResource.class, dfe);
    }
}

