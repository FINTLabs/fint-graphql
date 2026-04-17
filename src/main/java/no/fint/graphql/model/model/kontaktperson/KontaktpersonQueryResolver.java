
package no.fint.graphql.model.model.kontaktperson;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.KontaktpersonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelKontaktpersonQueryResolver")
@Slf4j
public class KontaktpersonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KontaktpersonService service;

    public CompletionStage<KontaktpersonResource> getKontaktperson(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kontaktperson");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontaktpersonResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KontaktpersonResource>empty().toFuture();
    }
}
