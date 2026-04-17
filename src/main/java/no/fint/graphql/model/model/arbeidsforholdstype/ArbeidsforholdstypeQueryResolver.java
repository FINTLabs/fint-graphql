
package no.fint.graphql.model.model.arbeidsforholdstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelArbeidsforholdstypeQueryResolver")
@Slf4j
public class ArbeidsforholdstypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArbeidsforholdstypeService service;

    public CompletionStage<ArbeidsforholdstypeResource> getArbeidsforholdstype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Arbeidsforholdstype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArbeidsforholdstypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArbeidsforholdstypeResource>empty().toFuture();
    }
}
