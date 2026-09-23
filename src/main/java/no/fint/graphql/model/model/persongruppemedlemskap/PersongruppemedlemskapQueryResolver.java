
package no.fint.graphql.model.model.persongruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.PersongruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelPersongruppemedlemskapQueryResolver")
@Slf4j
public class PersongruppemedlemskapQueryResolver {

    @Autowired
    private PersongruppemedlemskapService service;

    @QueryMapping(name = "persongruppemedlemskap")
    public CompletionStage<PersongruppemedlemskapResource> persongruppemedlemskap(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Persongruppemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersongruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<PersongruppemedlemskapResource>empty().toFuture();
    }
}
