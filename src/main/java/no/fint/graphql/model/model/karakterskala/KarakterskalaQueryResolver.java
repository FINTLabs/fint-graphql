
package no.fint.graphql.model.model.karakterskala;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelKarakterskalaQueryResolver")
@Slf4j
public class KarakterskalaQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterskalaService service;

    public CompletionStage<KarakterskalaResource> getKarakterskala(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Karakterskala");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterskalaResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterskalaResource>empty().toFuture();
    }
}
