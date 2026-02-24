
package no.fint.graphql.model.model.klassemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.KlassemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelKlassemedlemskapQueryResolver")
@Slf4j
public class KlassemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KlassemedlemskapService service;

    public CompletionStage<KlassemedlemskapResource> klassemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Klassemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKlassemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KlassemedlemskapResource>empty().toFuture();
    }
}
