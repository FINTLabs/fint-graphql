
package no.fint.graphql.model.felles.kommune;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.KommuneResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesKommuneService")
public class KommuneService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KommuneResource getKommuneResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKommuneResource(
            endpoints.getFellesKodeverk() 
                + "/kommune/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public KommuneResource getKommuneResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KommuneResource.class, dfe);
    }
}

