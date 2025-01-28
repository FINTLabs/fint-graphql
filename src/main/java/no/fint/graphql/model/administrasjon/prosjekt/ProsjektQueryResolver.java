
package no.fint.graphql.model.administrasjon.prosjekt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonProsjektQueryResolver")
@Slf4j
public class ProsjektQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ProsjektService service;

    public CompletionStage<ProsjektResource> getProsjekt(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Prosjekt");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getProsjektResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ProsjektResource>empty().toFuture();
    }
}
