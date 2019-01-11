// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.personalressurs;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonPersonalressursService")
public class PersonalressursService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public PersonalressursResources getPersonalressursResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonPersonal() + "/personalressurs",
                    sinceTimeStamp),
                PersonalressursResources.class,
                dfe);
    }

    public PersonalressursResource getPersonalressursResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonalressursResource.class, dfe);
    }
}

