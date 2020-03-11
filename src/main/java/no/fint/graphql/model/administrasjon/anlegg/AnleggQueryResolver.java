
package no.fint.graphql.model.administrasjon.anlegg;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.AnleggResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonAnleggQueryResolver")
public class AnleggQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AnleggService service;

    public CompletionStage<AnleggResource> getAnlegg(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAnleggResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AnleggResource>empty().toFuture();
    }
}
