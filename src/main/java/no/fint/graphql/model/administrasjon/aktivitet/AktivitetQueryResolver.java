
package no.fint.graphql.model.administrasjon.aktivitet;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.AktivitetResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonAktivitetQueryResolver")
public class AktivitetQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AktivitetService service;

    public CompletionStage<AktivitetResource> getAktivitet(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAktivitetResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AktivitetResource>empty().toFuture();
    }
}