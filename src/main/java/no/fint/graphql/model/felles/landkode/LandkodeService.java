// Built from tag v3.1.0

package no.fint.graphql.model.felles.landkode;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fellesLandkodeService")
public class LandkodeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public LandkodeResources getLandkodeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getFellesKodeverkIso() + "/landkode",
                    sinceTimeStamp),
                LandkodeResources.class,
                dfe);
    }

    public LandkodeResource getLandkodeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, LandkodeResource.class, dfe);
    }
}

