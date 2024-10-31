
package no.fint.graphql.model.felles.person;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.PersonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("fellesPersonQueryResolver")
public class PersonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonService service;

    public CompletionStage<PersonResource> getPerson(
            String fodselsnummer,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(fodselsnummer)) {
            return service.getPersonResourceById("fodselsnummer", fodselsnummer, dfe).toFuture();
        }
        return Mono.<PersonResource>empty().toFuture();
    }
}
