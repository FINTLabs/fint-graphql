// Built from tag v3.1.0

package no.fint.graphql.model.felles.kjonn;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesKjonnService")
public class KjonnService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KjonnResources getKjonnResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getFellesKodeverkIso() + "/kjonn",
                    sinceTimeStamp),
                KjonnResources.class,
                dfe);
    }

    public KjonnResource getKjonnResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KjonnResource.class, dfe);
    }
}

