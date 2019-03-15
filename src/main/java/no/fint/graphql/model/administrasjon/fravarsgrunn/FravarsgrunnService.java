// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fravarsgrunn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonFravarsgrunnService")
public class FravarsgrunnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FravarsgrunnResource getFravarsgrunnResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFravarsgrunnResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/fravarsgrunn/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public FravarsgrunnResource getFravarsgrunnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FravarsgrunnResource.class, dfe);
    }
}

