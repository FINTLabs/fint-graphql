
package no.fint.graphql.model.administrasjon.fasttillegg;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonFasttilleggQueryResolver")
public class FasttilleggQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FasttilleggService service;

    public CompletionStage<FasttilleggResource> getFasttillegg(
            String kildesystemId,
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(kildesystemId)) {
            return service.getFasttilleggResourceById("kildesystemid", kildesystemId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFasttilleggResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FasttilleggResource>empty().toFuture();
    }
}
