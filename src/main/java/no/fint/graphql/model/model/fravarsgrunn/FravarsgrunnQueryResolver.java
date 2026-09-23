
package no.fint.graphql.model.model.fravarsgrunn;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFravarsgrunnQueryResolver")
@Slf4j
public class FravarsgrunnQueryResolver {

    @Autowired
    private FravarsgrunnService service;

    public CompletionStage<FravarsgrunnResource> fravarsgrunn(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Fravarsgrunn");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarsgrunnResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<FravarsgrunnResource>empty().toFuture();
    }
}
