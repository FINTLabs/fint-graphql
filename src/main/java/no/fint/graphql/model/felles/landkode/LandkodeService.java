// Built from tag release-3.2

package no.fint.graphql.model.felles.landkode;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesLandkodeService")
public class LandkodeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public LandkodeResource getLandkodeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getLandkodeResource(
            endpoints.getFellesKodeverkIso() 
                + "/landkode/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public LandkodeResource getLandkodeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, LandkodeResource.class, dfe);
    }
}

