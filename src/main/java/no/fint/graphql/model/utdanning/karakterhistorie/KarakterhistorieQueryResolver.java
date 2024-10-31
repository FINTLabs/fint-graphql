
package no.fint.graphql.model.utdanning.karakterhistorie;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.KarakterhistorieResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningKarakterhistorieQueryResolver")
public class KarakterhistorieQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterhistorieService service;

    public CompletionStage<KarakterhistorieResource> getKarakterhistorie(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterhistorieResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterhistorieResource>empty().toFuture();
    }
}
