
package no.fint.graphql.model.administrasjon.personalressurskategori;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("administrasjonPersonalressurskategoriService")
public class PersonalressurskategoriService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<PersonalressurskategoriResource> getPersonalressurskategoriResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getPersonalressurskategoriResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/personalressurskategori/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<PersonalressurskategoriResource> getPersonalressurskategoriResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonalressurskategoriResource.class, dfe);
    }
}

