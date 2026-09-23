
package no.fint.graphql.model.model.persongruppe;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.PersongruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelPersongruppeQueryResolver")
@Slf4j
public class PersongruppeQueryResolver {

    @Autowired
    private PersongruppeService service;

    @QueryMapping(name = "persongruppe")
    public CompletionStage<PersongruppeResource> persongruppe(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Persongruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersongruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<PersongruppeResource>empty().toFuture();
    }
}
