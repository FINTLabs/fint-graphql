
package no.fint.graphql.model.model.arbeidsforhold;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelArbeidsforholdQueryResolver")
@Slf4j
public class ArbeidsforholdQueryResolver {

    @Autowired
    private ArbeidsforholdService service;

    @QueryMapping(name = "arbeidsforhold")
    public CompletionStage<ArbeidsforholdResource> arbeidsforhold(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Arbeidsforhold");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArbeidsforholdResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ArbeidsforholdResource>empty().toFuture();
    }
}
