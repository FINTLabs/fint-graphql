
package no.fint.graphql.model.model.organisasjonselement;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelOrganisasjonselementQueryResolver")
@Slf4j
public class OrganisasjonselementQueryResolver {

    @Autowired
    private OrganisasjonselementService service;

    @QueryMapping(name = "organisasjonselement")
    public CompletionStage<OrganisasjonselementResource> organisasjonselement(
            @Argument("organisasjonsId") String organisasjonsId,
            @Argument("organisasjonsKode") String organisasjonsKode,
            @Argument("organisasjonsnummer") String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Organisasjonselement");
        if (StringUtils.isNotEmpty(organisasjonsId)) {
            return service.getOrganisasjonselementResourceById("organisasjonsid", organisasjonsId, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(organisasjonsKode)) {
            return service.getOrganisasjonselementResourceById("organisasjonskode", organisasjonsKode, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(organisasjonsnummer)) {
            return service.getOrganisasjonselementResourceById("organisasjonsnummer", organisasjonsnummer, dfe).toFuture();
        }
        return Mono.<OrganisasjonselementResource>empty().toFuture();
    }
}
