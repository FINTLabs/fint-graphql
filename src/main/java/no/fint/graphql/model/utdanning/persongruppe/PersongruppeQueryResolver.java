
package no.fint.graphql.model.utdanning.persongruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.PersongruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningPersongruppeQueryResolver")
public class PersongruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersongruppeService service;

    public CompletionStage<PersongruppeResource> getPersongruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersongruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<PersongruppeResource>empty().toFuture();
    }
}
