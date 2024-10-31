
package no.fint.graphql.model.administrasjon.arbeidslokasjon;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.organisasjon.ArbeidslokasjonResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonArbeidslokasjonQueryResolver")
public class ArbeidslokasjonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArbeidslokasjonService service;

    public CompletionStage<ArbeidslokasjonResource> getArbeidslokasjon(
            String lokasjonskode,
            String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(lokasjonskode)) {
            return service.getArbeidslokasjonResourceById("lokasjonskode", lokasjonskode, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(organisasjonsnummer)) {
            return service.getArbeidslokasjonResourceById("organisasjonsnummer", organisasjonsnummer, dfe).toFuture();
        }
        return Mono.<ArbeidslokasjonResource>empty().toFuture();
    }
}
