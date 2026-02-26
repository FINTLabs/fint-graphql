
package no.fint.graphql.model.model.elevforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelElevforholdQueryResolver")
@Slf4j
public class ElevforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevforholdService service;

    public CompletionStage<ElevforholdResource> elevforhold(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elevforhold");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevforholdResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevforholdResource>empty().toFuture();
    }
}
