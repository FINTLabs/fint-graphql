
package no.fint.graphql.model.model.otungdom;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.ot.OtUngdomResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelOtUngdomQueryResolver")
@Slf4j
public class OtUngdomQueryResolver {

    @Autowired
    private OtUngdomService service;

    @QueryMapping(name = "otungdom")
    public CompletionStage<OtUngdomResource> otungdom(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for OtUngdom");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getOtUngdomResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<OtUngdomResource>empty().toFuture();
    }
}
