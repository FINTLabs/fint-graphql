
package no.fint.graphql.model.utdanning.medlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningMedlemskapQueryResolver")
@Slf4j
public class MedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private MedlemskapService service;

    public CompletionStage<MedlemskapResource> getMedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Medlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getMedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<MedlemskapResource>empty().toFuture();
    }
}
