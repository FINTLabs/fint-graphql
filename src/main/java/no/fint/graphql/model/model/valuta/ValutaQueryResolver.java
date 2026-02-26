
package no.fint.graphql.model.model.valuta;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.felles.kodeverk.ValutaResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelValutaQueryResolver")
@Slf4j
public class ValutaQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ValutaService service;

    public CompletionStage<ValutaResource> valuta(
            String bokstavkode,
            String nummerkode,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Valuta");
        if (StringUtils.isNotEmpty(bokstavkode)) {
            return service.getValutaResourceById("bokstavkode", bokstavkode, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(nummerkode)) {
            return service.getValutaResourceById("nummerkode", nummerkode, dfe).toFuture();
        }
        return Mono.<ValutaResource>empty().toFuture();
    }
}
