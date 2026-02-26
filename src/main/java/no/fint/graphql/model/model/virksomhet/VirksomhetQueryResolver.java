
package no.fint.graphql.model.model.virksomhet;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.VirksomhetResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelVirksomhetQueryResolver")
@Slf4j
public class VirksomhetQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VirksomhetService service;

    public CompletionStage<VirksomhetResource> virksomhet(
            String virksomhetsId,
            String organisasjonsnummer,
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
