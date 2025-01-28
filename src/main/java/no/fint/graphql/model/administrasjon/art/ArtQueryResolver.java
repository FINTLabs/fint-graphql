
package no.fint.graphql.model.administrasjon.art;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonArtQueryResolver")
@Slf4j
public class ArtQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArtService service;

    public CompletionStage<ArtResource> getArt(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Art");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArtResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArtResource>empty().toFuture();
    }
}
