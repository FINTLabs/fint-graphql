
package no.fint.graphql.model.model.eksamensgruppe;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelEksamensgruppeQueryResolver")
@Slf4j
public class EksamensgruppeQueryResolver {

    @Autowired
    private EksamensgruppeService service;

    @QueryMapping(name = "eksamensgruppe")
    public CompletionStage<EksamensgruppeResource> eksamensgruppe(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Eksamensgruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamensgruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamensgruppeResource>empty().toFuture();
    }
}
