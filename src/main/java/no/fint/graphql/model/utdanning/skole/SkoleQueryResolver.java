
package no.fint.graphql.model.utdanning.skole;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSkoleQueryResolver")
public class SkoleQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleService service;

    public CompletionStage<SkoleResource> getSkole(
            String skolenummer,
            String systemId,
            String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
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
