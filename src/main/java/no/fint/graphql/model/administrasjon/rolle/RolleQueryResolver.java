
package no.fint.graphql.model.administrasjon.rolle;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.fullmakt.RolleResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonRolleQueryResolver")
@Slf4j
public class RolleQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private RolleService service;

    public CompletionStage<RolleResource> getRolle(
            String navn,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Rolle");
        if (StringUtils.isNotEmpty(navn)) {
            return service.getRolleResourceById("navn", navn, dfe).toFuture();
        }
        return Mono.<RolleResource>empty().toFuture();
    }
}
