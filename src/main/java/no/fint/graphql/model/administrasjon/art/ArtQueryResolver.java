
package no.fint.graphql.model.administrasjon.art;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonArtQueryResolver")
public class ArtQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArtService service;

    public CompletionStage<ArtResource> getArt(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArtResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArtResource>empty().toFuture();
    }
}