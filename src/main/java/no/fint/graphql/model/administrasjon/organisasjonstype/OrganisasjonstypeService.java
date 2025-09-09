
package no.fint.graphql.model.administrasjon.organisasjonstype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.OrganisasjonstypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonOrganisasjonstypeService")
public class OrganisasjonstypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<OrganisasjonstypeResource> getOrganisasjonstypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getOrganisasjonstypeResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/organisasjonstype/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<OrganisasjonstypeResource> getOrganisasjonstypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, OrganisasjonstypeResource.class, dfe);
    }
}

