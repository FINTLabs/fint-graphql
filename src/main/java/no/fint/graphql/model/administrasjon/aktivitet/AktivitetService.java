
package no.fint.graphql.model.administrasjon.aktivitet;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.AktivitetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonAktivitetService")
public class AktivitetService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<AktivitetResource> getAktivitetResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getAktivitetResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/aktivitet/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<AktivitetResource> getAktivitetResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AktivitetResource.class, dfe);
    }
}

