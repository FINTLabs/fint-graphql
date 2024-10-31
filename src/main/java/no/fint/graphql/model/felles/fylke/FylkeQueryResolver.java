
package no.fint.graphql.model.felles.fylke;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("fellesFylkeQueryResolver")
public class FylkeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FylkeService service;

    public CompletionStage<FylkeResource> getFylke(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFylkeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FylkeResource>empty().toFuture();
    }
}
