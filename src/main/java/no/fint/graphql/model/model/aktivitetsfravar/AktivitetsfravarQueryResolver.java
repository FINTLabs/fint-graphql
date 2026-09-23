
package no.fint.graphql.model.model.aktivitetsfravar;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.AktivitetsfravarResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAktivitetsfravarQueryResolver")
@Slf4j
public class AktivitetsfravarQueryResolver {

    @Autowired
    private AktivitetsfravarService service;

    @QueryMapping(name = "aktivitetsfravar")
    public CompletionStage<AktivitetsfravarResource> aktivitetsfravar(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Aktivitetsfravar");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAktivitetsfravarResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AktivitetsfravarResource>empty().toFuture();
    }
}
