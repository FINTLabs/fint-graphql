
package no.fint.graphql.model.administrasjon.kontrakt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.KontraktResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonKontraktQueryResolver")
@Slf4j
public class KontraktQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KontraktService service;

    public CompletionStage<KontraktResource> getKontrakt(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kontrakt");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontraktResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KontraktResource>empty().toFuture();
    }
}
