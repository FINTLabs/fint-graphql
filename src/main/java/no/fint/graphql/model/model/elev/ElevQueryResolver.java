
package no.fint.graphql.model.model.elev;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.elev.ElevResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelElevQueryResolver")
@Slf4j
public class ElevQueryResolver {

    @Autowired
    private ElevService service;

    @QueryMapping(name = "elev")
    public CompletionStage<ElevResource> elev(
            @Argument("brukernavn") String brukernavn,
            @Argument("elevnummer") String elevnummer,
            @Argument("feidenavn") String feidenavn,
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Elev");
        if (StringUtils.isNotEmpty(brukernavn)) {
            return service.getElevResourceById("brukernavn", brukernavn, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(elevnummer)) {
            return service.getElevResourceById("elevnummer", elevnummer, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(feidenavn)) {
            return service.getElevResourceById("feidenavn", feidenavn, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevResource>empty().toFuture();
    }
}
