
package no.fint.graphql.model.model.klassemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.KlassemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKlassemedlemskapQueryResolver")
@Slf4j
public class KlassemedlemskapQueryResolver {

    @Autowired
    private KlassemedlemskapService service;

    @QueryMapping(name = "klassemedlemskap")
    public CompletionStage<KlassemedlemskapResource> klassemedlemskap(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Klassemedlemskap");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKlassemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<KlassemedlemskapResource>empty().toFuture();
    }
}
