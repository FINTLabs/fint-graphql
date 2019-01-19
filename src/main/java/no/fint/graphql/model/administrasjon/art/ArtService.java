// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.art;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonArtService")
public class ArtService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ArtResources getArtResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonKodeverk() + "/art",
                    sinceTimeStamp),
                ArtResources.class,
                dfe);
    }

    public ArtResource getArtResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ArtResource.class, dfe);
    }
}

