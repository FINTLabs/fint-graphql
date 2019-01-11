// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.ansvar;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonAnsvarService")
public class AnsvarService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public AnsvarResources getAnsvarResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonKodeverk() + "/ansvar",
                    sinceTimeStamp),
                AnsvarResources.class,
                dfe);
    }

    public AnsvarResource getAnsvarResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AnsvarResource.class, dfe);
    }
}

