
package no.fint.graphql.model.model.art;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.ArtResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelArtQueryResolver")
@Slf4j
public class ArtQueryResolver {

    @Autowired
    private ArtService service;

    public CompletionStage<ArtResource> art(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Art");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArtResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArtResource>empty().toFuture();
    }
}
