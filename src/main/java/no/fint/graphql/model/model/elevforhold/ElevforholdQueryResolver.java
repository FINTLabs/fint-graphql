
package no.fint.graphql.model.model.elevforhold;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelElevforholdQueryResolver")
@Slf4j
public class ElevforholdQueryResolver {

    @Autowired
    private ElevforholdService service;

    @QueryMapping(name = "elevforhold")
    public CompletionStage<ElevforholdResource> elevforhold(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elevforhold");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevforholdResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevforholdResource>empty().toFuture();
    }
}
