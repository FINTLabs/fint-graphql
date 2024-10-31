
package no.fint.graphql.model.felles.sprak;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("fellesSprakQueryResolver")
public class SprakQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SprakService service;

    public CompletionStage<SprakResource> getSprak(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSprakResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SprakResource>empty().toFuture();
    }
}