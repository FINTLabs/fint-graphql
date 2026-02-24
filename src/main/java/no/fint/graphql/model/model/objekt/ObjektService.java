
package no.fint.graphql.model.model.objekt;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.kodeverk.ObjektResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelObjektService")
public class ObjektService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<ObjektResource> getObjektResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getObjektResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/objekt/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<ObjektResource> getObjektResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ObjektResource.class, dfe);
    }
}

