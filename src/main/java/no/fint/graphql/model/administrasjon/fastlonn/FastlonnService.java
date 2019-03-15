// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fastlonn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonFastlonnService")
public class FastlonnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public FastlonnResource getFastlonnResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getFastlonnResource(
            endpoints.getAdministrasjonPersonal() 
                + "/fastlonn/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public FastlonnResource getFastlonnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, FastlonnResource.class, dfe);
    }
}

