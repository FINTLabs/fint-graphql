// Built from tag release-3.2

package no.fint.graphql.model.utdanning.elevkategori;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningElevkategoriService")
public class ElevkategoriService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ElevkategoriResource getElevkategoriResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getElevkategoriResource(
            endpoints.getUtdanningKodeverk() 
                + "/elevkategori/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public ElevkategoriResource getElevkategoriResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ElevkategoriResource.class, dfe);
    }
}

