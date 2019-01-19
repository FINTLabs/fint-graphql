// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsgruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("utdanningUndervisningsgruppeService")
public class UndervisningsgruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public UndervisningsgruppeResources getUndervisningsgruppeResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getUtdanningTimeplan() + "/undervisningsgruppe",
                    sinceTimeStamp),
                UndervisningsgruppeResources.class,
                dfe);
    }

    public UndervisningsgruppeResource getUndervisningsgruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UndervisningsgruppeResource.class, dfe);
    }
}

