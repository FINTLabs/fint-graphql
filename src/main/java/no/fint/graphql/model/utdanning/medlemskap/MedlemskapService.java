// Built from tag release-3.2

package no.fint.graphql.model.utdanning.medlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningMedlemskapService")
public class MedlemskapService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public MedlemskapResource getMedlemskapResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getMedlemskapResource(
            endpoints.getUtdanningElev() 
                + "/medlemskap/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public MedlemskapResource getMedlemskapResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, MedlemskapResource.class, dfe);
    }
}

