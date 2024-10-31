
package no.fint.graphql.model.utdanning.persongruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.PersongruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningPersongruppemedlemskapQueryResolver")
public class PersongruppemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersongruppemedlemskapService service;

    public CompletionStage<PersongruppemedlemskapResource> getPersongruppemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersongruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<PersongruppemedlemskapResource>empty().toFuture();
    }
}
