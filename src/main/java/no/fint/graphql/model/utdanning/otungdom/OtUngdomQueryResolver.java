
package no.fint.graphql.model.utdanning.otungdom;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.ot.OtUngdomResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningOtUngdomQueryResolver")
@Slf4j
public class OtUngdomQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OtUngdomService service;

    public CompletionStage<OtUngdomResource> getOtungdom(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for OtUngdom");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getOtUngdomResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<OtUngdomResource>empty().toFuture();
    }
}
