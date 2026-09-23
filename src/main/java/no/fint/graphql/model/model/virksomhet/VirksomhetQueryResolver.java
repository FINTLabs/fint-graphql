
package no.fint.graphql.model.model.virksomhet;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.VirksomhetResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelVirksomhetQueryResolver")
@Slf4j
public class VirksomhetQueryResolver {

    @Autowired
    private VirksomhetService service;

    @QueryMapping(name = "virksomhet")
    public CompletionStage<VirksomhetResource> virksomhet(
            @Argument("virksomhetsId") String virksomhetsId,
            @Argument("organisasjonsnummer") String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Virksomhet");
        if (StringUtils.isNotEmpty(virksomhetsId)) {
            return service.getVirksomhetResourceById("virksomhetsid", virksomhetsId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(organisasjonsnummer)) {
            return service.getVirksomhetResourceById("organisasjonsnummer", organisasjonsnummer, dfe).toFuture();
        }
        return Mono.<VirksomhetResource>empty().toFuture();
    }
}
