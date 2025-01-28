
package no.fint.graphql.model.utdanning.eksamen;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.timeplan.EksamenResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningEksamenQueryResolver")
@Slf4j
public class EksamenQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private EksamenService service;

    public CompletionStage<EksamenResource> getEksamen(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Eksamen");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamenResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamenResource>empty().toFuture();
    }
}
