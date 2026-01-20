
package no.fint.graphql.model.model.personalressurs;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.administrasjon.personal.PersonalressursResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelPersonalressursService")
public class PersonalressursService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<PersonalressursResource> getPersonalressursResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getPersonalressursResource(
            endpoints.getAdministrasjonPersonal() 
                + "/personalressurs/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<PersonalressursResource> getPersonalressursResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonalressursResource.class, dfe);
    }
}

