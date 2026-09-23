
package no.fint.graphql.model.model.kontaktlarergruppe;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKontaktlarergruppeQueryResolver")
@Slf4j
public class KontaktlarergruppeQueryResolver {

    @Autowired
    private KontaktlarergruppeService service;

    @QueryMapping(name = "kontaktlarergruppe")
    public CompletionStage<KontaktlarergruppeResource> kontaktlarergruppe(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kontaktlarergruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontaktlarergruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KontaktlarergruppeResource>empty().toFuture();
    }
}
