
package no.fint.graphql.model.utdanning.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSkoleressursQueryResolver")
@Slf4j
public class SkoleressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleressursService service;

    public CompletionStage<SkoleressursResource> getSkoleressurs(
            String feidenavn,
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Skoleressurs");
        if (StringUtils.isNotEmpty(feidenavn)) {
            return service.getSkoleressursResourceById("feidenavn", feidenavn, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleressursResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SkoleressursResource>empty().toFuture();
    }
}
