
package no.fint.graphql.model.felles.landkode;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("fellesLandkodeQueryResolver")
@Slf4j
public class LandkodeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LandkodeService service;

    public CompletionStage<LandkodeResource> getLandkode(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Landkode");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLandkodeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<LandkodeResource>empty().toFuture();
    }
}
