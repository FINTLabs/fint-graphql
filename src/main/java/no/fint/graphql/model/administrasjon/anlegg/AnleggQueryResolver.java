
package no.fint.graphql.model.administrasjon.anlegg;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.AnleggResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonAnleggQueryResolver")
@Slf4j
public class AnleggQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AnleggService service;

    public CompletionStage<AnleggResource> getAnlegg(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Anlegg");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAnleggResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AnleggResource>empty().toFuture();
    }
}
