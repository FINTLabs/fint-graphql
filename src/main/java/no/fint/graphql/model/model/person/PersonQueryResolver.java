
package no.fint.graphql.model.model.person;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.PersonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelPersonQueryResolver")
@Slf4j
public class PersonQueryResolver {

    @Autowired
    private PersonService service;

    @QueryMapping(name = "person")
    public CompletionStage<PersonResource> person(
            @Argument("fodselsnummer") String fodselsnummer,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Person");
        if (StringUtils.isNotEmpty(fodselsnummer)) {
            return service.getPersonResourceById("fodselsnummer", fodselsnummer, dfe).toFuture();
        }
        return Mono.<PersonResource>empty().toFuture();
    }
}
