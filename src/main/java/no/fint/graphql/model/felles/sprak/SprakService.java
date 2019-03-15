// Built from tag release-3.2

package no.fint.graphql.model.felles.sprak;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesSprakService")
public class SprakService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public SprakResource getSprakResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getSprakResource(
            endpoints.getFellesKodeverkIso() 
                + "/sprak/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public SprakResource getSprakResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, SprakResource.class, dfe);
    }
}

