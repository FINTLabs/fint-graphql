
package no.fint.graphql.model.model.kontaktperson;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.KontaktpersonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKontaktpersonQueryResolver")
@Slf4j
public class KontaktpersonQueryResolver {

    @Autowired
    private KontaktpersonService service;

    @QueryMapping(name = "kontaktperson")
    public CompletionStage<KontaktpersonResource> kontaktperson(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Kontaktperson");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKontaktpersonResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KontaktpersonResource>empty().toFuture();
    }
}
