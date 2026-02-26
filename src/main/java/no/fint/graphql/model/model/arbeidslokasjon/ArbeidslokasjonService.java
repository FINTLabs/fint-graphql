
package no.fint.graphql.model.model.arbeidslokasjon;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.organisasjon.ArbeidslokasjonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelArbeidslokasjonService")
public class ArbeidslokasjonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ArbeidslokasjonResource> getArbeidslokasjonResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getArbeidslokasjonResource(
            endpoints.getAdministrasjonOrganisasjon() 
                + "/arbeidslokasjon/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ArbeidslokasjonResource> getArbeidslokasjonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ArbeidslokasjonResource.class, dfe);
    }
}

