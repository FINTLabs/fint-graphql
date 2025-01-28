
package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningKontaktlarergruppeQueryResolver")
@Slf4j
public class KontaktlarergruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KontaktlarergruppeService service;

    public CompletionStage<KontaktlarergruppeResource> getKontaktlarergruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kontaktlarergruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontaktlarergruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KontaktlarergruppeResource>empty().toFuture();
    }
}
