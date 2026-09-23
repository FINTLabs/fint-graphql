
package no.fint.graphql.model.model.uketimetall;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelUketimetallQueryResolver")
@Slf4j
public class UketimetallQueryResolver {

    @Autowired
    private UketimetallService service;

    public CompletionStage<UketimetallResource> uketimetall(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Uketimetall");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUketimetallResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UketimetallResource>empty().toFuture();
    }
}
