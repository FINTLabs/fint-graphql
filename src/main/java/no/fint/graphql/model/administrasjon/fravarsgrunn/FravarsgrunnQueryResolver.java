
package no.fint.graphql.model.administrasjon.fravarsgrunn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonFravarsgrunnQueryResolver")
@Slf4j
public class FravarsgrunnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarsgrunnService service;

    public CompletionStage<FravarsgrunnResource> getFravarsgrunn(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fravarsgrunn");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarsgrunnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FravarsgrunnResource>empty().toFuture();
    }
}
