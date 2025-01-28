
package no.fint.graphql.model.administrasjon.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonArbeidsforholdQueryResolver")
@Slf4j
public class ArbeidsforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArbeidsforholdService service;

    public CompletionStage<ArbeidsforholdResource> getArbeidsforhold(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Arbeidsforhold");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArbeidsforholdResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArbeidsforholdResource>empty().toFuture();
    }
}
