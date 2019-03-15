// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.uketimetall;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonUketimetallService")
public class UketimetallService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public UketimetallResource getUketimetallResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getUketimetallResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/uketimetall/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public UketimetallResource getUketimetallResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UketimetallResource.class, dfe);
    }
}

