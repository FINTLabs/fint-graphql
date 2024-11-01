
package no.fint.graphql.model.utdanning.elevkategori;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningElevkategoriQueryResolver")
public class ElevkategoriQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevkategoriService service;

    public CompletionStage<ElevkategoriResource> getElevkategori(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevkategoriResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevkategoriResource>empty().toFuture();
    }
}
