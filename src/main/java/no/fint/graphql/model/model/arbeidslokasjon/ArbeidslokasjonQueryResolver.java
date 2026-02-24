
package no.fint.graphql.model.model.arbeidslokasjon;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.organisasjon.ArbeidslokasjonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelArbeidslokasjonQueryResolver")
@Slf4j
public class ArbeidslokasjonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArbeidslokasjonService service;

    public CompletionStage<ArbeidslokasjonResource> arbeidslokasjon(
            String lokasjonskode,
            String organisasjonsnummer,
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
