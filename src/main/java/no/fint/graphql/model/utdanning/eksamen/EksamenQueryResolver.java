
package no.fint.graphql.model.utdanning.eksamen;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.timeplan.EksamenResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningEksamenQueryResolver")
public class EksamenQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private EksamenService service;

    public CompletionStage<EksamenResource> getEksamen(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamenResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamenResource>empty().toFuture();
    }
}
