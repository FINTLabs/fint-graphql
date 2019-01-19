// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterskala;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningKarakterskalaService")
public class KarakterskalaService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public KarakterskalaResources getKarakterskalaResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningKodeverk() + "/karakterskala",
                    sinceTimeStamp),
                KarakterskalaResources.class,
                dfe);
    }

    public KarakterskalaResource getKarakterskalaResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, KarakterskalaResource.class, dfe);
    }
}

