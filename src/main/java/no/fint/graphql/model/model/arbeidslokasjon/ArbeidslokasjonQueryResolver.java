
package no.fint.graphql.model.model.arbeidslokasjon;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.organisasjon.ArbeidslokasjonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelArbeidslokasjonQueryResolver")
@Slf4j
public class ArbeidslokasjonQueryResolver {

    @Autowired
    private ArbeidslokasjonService service;

    @QueryMapping(name = "arbeidslokasjon")
    public CompletionStage<ArbeidslokasjonResource> arbeidslokasjon(
            @Argument("lokasjonskode") String lokasjonskode,
            @Argument("organisasjonsnummer") String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Arbeidslokasjon");
        if (StringUtils.isNotEmpty(lokasjonskode)) {
            return service.getArbeidslokasjonResourceById("lokasjonskode", lokasjonskode, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(organisasjonsnummer)) {
            return service.getArbeidslokasjonResourceById("organisasjonsnummer", organisasjonsnummer, dfe).toFuture();
        }
        return Mono.<ArbeidslokasjonResource>empty().toFuture();
    }
}
