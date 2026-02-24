
package no.fint.graphql.model.model.uketimetall;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelUketimetallQueryResolver")
@Slf4j
public class UketimetallQueryResolver implements GraphQLQueryResolver {

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
