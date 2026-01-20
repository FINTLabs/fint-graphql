
package no.fint.graphql.model.model.ansvar;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelAnsvarService")
public class AnsvarService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<AnsvarResource> getAnsvarResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getAnsvarResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/ansvar/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<AnsvarResource> getAnsvarResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AnsvarResource.class, dfe);
    }
}

