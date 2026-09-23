
package no.fint.graphql.model.model.skole;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSkoleQueryResolver")
@Slf4j
public class SkoleQueryResolver {

    @Autowired
    private SkoleService service;

    @QueryMapping(name = "skole")
    public CompletionStage<SkoleResource> skole(
            @Argument("skolenummer") String skolenummer,
            @Argument("systemId") String systemId,
            @Argument("organisasjonsnummer") String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Skole");
        if (StringUtils.isNotEmpty(skolenummer)) {
            return service.getSkoleResourceById("skolenummer", skolenummer, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleResourceById("systemid", systemId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(organisasjonsnummer)) {
            return service.getSkoleResourceById("organisasjonsnummer", organisasjonsnummer, dfe).toFuture();
        }
        return Mono.<SkoleResource>empty().toFuture();
    }
}
