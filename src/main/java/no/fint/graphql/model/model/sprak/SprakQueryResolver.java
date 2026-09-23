
package no.fint.graphql.model.model.sprak;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.kodeverk.iso.SprakResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSprakQueryResolver")
@Slf4j
public class SprakQueryResolver {

    @Autowired
    private SprakService service;

    public CompletionStage<SprakResource> sprak(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Sprak");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSprakResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SprakResource>empty().toFuture();
    }
}
