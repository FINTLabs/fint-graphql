
package no.fint.graphql.model.model.aktivitet;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.AktivitetResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAktivitetQueryResolver")
@Slf4j
public class AktivitetQueryResolver {

    @Autowired
    private AktivitetService service;

    public CompletionStage<AktivitetResource> aktivitet(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Aktivitet");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAktivitetResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AktivitetResource>empty().toFuture();
    }
}
