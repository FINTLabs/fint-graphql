
package no.fint.graphql.model.model.eksamen;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.EksamenResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelEksamenQueryResolver")
@Slf4j
public class EksamenQueryResolver {

    @Autowired
    private EksamenService service;

    @QueryMapping(name = "eksamen")
    public CompletionStage<EksamenResource> eksamen(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Eksamen");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamenResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamenResource>empty().toFuture();
    }
}
