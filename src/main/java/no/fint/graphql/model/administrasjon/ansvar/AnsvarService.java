// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.ansvar;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonAnsvarService")
public class AnsvarService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public AnsvarResource getAnsvarResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getAnsvarResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/ansvar/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public AnsvarResource getAnsvarResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, AnsvarResource.class, dfe);
    }
}

