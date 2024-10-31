
package no.fint.graphql.model.utdanning.eksamensform;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.EksamensformResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("utdanningEksamensformService")
public class EksamensformService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<EksamensformResource> getEksamensformResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getEksamensformResource(
            endpoints.getUtdanningKodeverk() 
                + "/eksamensform/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<EksamensformResource> getEksamensformResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, EksamensformResource.class, dfe);
    }
}

