
package no.fint.graphql.model.utdanning.eksamensform;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.kodeverk.EksamensformResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningEksamensformQueryResolver")
@Slf4j
public class EksamensformQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private EksamensformService service;

    public CompletionStage<EksamensformResource> getEksamensform(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Eksamensform");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamensformResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamensformResource>empty().toFuture();
    }
}
