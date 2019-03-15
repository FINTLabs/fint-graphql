// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fravar;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.FravarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonFravarService")
public class FravarService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FravarResource getFravarResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFravarResource(
            endpoints.getAdministrasjonPersonal() 
                + "/fravar/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public FravarResource getFravarResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FravarResource.class, dfe);
    }
}

