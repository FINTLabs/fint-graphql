
package no.fint.graphql.model.model.arbeidsforholdstype;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelArbeidsforholdstypeQueryResolver")
@Slf4j
public class ArbeidsforholdstypeQueryResolver {

    @Autowired
    private ArbeidsforholdstypeService service;

    public CompletionStage<ArbeidsforholdstypeResource> arbeidsforholdstype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Arbeidsforholdstype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArbeidsforholdstypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArbeidsforholdstypeResource>empty().toFuture();
    }
}
