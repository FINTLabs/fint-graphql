
package no.fint.graphql.model.model.undervisningsforhold;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("modelUndervisningsforholdService")
public class UndervisningsforholdService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<UndervisningsforholdResource> getUndervisningsforholdResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getUndervisningsforholdResource(
            endpoints.getUtdanningElev() 
                + "/undervisningsforhold/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public Mono<UndervisningsforholdResource> getUndervisningsforholdResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, UndervisningsforholdResource.class, dfe);
    }
}

