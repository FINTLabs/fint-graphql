
package no.fint.graphql.model.utdanning.karakterskala;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningKarakterskalaQueryResolver")
public class KarakterskalaQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterskalaService service;

    public CompletionStage<KarakterskalaResource> getKarakterskala(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterskalaResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KarakterskalaResource>empty().toFuture();
    }
}
