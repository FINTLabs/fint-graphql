
package no.fint.graphql.model.felles.virksomhet;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.felles.VirksomhetResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("fellesVirksomhetQueryResolver")
public class VirksomhetQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VirksomhetService service;

    public CompletionStage<VirksomhetResource> getVirksomhet(
            String virksomhetsId,
            String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(virksomhetsId)) {
            return service.getVirksomhetResourceById("virksomhetsid", virksomhetsId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(organisasjonsnummer)) {
            return service.getVirksomhetResourceById("organisasjonsnummer", organisasjonsnummer, dfe).toFuture();
        }
        return Mono.<VirksomhetResource>empty().toFuture();
    }
}
