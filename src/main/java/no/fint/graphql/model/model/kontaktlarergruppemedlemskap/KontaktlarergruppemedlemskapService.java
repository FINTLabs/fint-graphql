
package no.fint.graphql.model.model.kontaktlarergruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelKontaktlarergruppemedlemskapService")
public class KontaktlarergruppemedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<KontaktlarergruppemedlemskapResource> getKontaktlarergruppemedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKontaktlarergruppemedlemskapResource(
            endpoints.getUtdanningElev() 
                + "/kontaktlarergruppemedlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<KontaktlarergruppemedlemskapResource> getKontaktlarergruppemedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KontaktlarergruppemedlemskapResource.class, dfe);
    }
}

