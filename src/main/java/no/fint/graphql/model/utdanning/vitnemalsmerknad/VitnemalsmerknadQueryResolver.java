
package no.fint.graphql.model.utdanning.vitnemalsmerknad;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.VitnemalsmerknadResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningVitnemalsmerknadQueryResolver")
@Slf4j
public class VitnemalsmerknadQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VitnemalsmerknadService service;

    public CompletionStage<VitnemalsmerknadResource> getVitnemalsmerknad(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Vitnemalsmerknad");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getVitnemalsmerknadResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<VitnemalsmerknadResource>empty().toFuture();
    }
}
