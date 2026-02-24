
package no.fint.graphql.model.model.lopenummer;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.LopenummerResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelLopenummerQueryResolver")
@Slf4j
public class LopenummerQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private LopenummerService service;

    public CompletionStage<LopenummerResource> lopenummer(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Lopenummer");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getLopenummerResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<LopenummerResource>empty().toFuture();
    }
}
