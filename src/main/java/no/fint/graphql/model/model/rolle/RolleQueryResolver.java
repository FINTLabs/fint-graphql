
package no.fint.graphql.model.model.rolle;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.fullmakt.RolleResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelRolleQueryResolver")
@Slf4j
public class RolleQueryResolver {

    @Autowired
    private RolleService service;

    @QueryMapping(name = "rolle")
    public CompletionStage<RolleResource> rolle(
            @Argument("navn") String navn,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Rolle");
        if (StringUtils.isNotEmpty(navn)) {
            return service.getRolleResourceById("navn", navn, dfe).toFuture();
        }
        return Mono.<RolleResource>empty().toFuture();
    }
}
