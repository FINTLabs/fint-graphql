
package no.fint.graphql.model.felles.kjonn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesKjonnService")
public class KjonnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KjonnResource getKjonnResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getKjonnResource(
            endpoints.getFellesKodeverkIso() 
                + "/kjonn/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public KjonnResource getKjonnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KjonnResource.class, dfe);
    }
}

