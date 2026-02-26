
package no.fint.graphql.model.model.elevkategori;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelElevkategoriQueryResolver")
@Slf4j
public class ElevkategoriQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevkategoriService service;

    public CompletionStage<ElevkategoriResource> elevkategori(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elevkategori");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevkategoriResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevkategoriResource>empty().toFuture();
    }
}
