
package no.fint.graphql.model.model.persongruppe;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.PersongruppeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelPersongruppeService")
public class PersongruppeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<PersongruppeResource> getPersongruppeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getPersongruppeResource(
            endpoints.getUtdanningElev() 
                + "/persongruppe/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<PersongruppeResource> getPersongruppeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersongruppeResource.class, dfe);
    }
}

