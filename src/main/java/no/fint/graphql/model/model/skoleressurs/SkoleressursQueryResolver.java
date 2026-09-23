
package no.fint.graphql.model.model.skoleressurs;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSkoleressursQueryResolver")
@Slf4j
public class SkoleressursQueryResolver {

    @Autowired
    private SkoleressursService service;

    @QueryMapping(name = "skoleressurs")
    public CompletionStage<SkoleressursResource> skoleressurs(
            @Argument("feidenavn") String feidenavn,
            @Argument("systemId") String systemId,
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
