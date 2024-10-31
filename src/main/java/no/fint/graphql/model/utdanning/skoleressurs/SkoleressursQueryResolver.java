
package no.fint.graphql.model.utdanning.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSkoleressursQueryResolver")
public class SkoleressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleressursService service;

    public CompletionStage<SkoleressursResource> getSkoleressurs(
            String feidenavn,
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(feidenavn)) {
            return service.getSkoleressursResourceById("feidenavn", feidenavn, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleressursResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SkoleressursResource>empty().toFuture();
    }
}