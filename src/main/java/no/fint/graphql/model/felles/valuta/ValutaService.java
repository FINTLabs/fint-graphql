
package no.fint.graphql.model.felles.valuta;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.ValutaResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("fellesValutaService")
public class ValutaService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ValutaResource> getValutaResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getValutaResource(
            endpoints.getFellesKodeverk() 
                + "/valuta/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ValutaResource> getValutaResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ValutaResource.class, dfe);
    }
}

