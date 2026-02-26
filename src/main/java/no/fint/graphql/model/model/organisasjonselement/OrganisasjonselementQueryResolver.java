
package no.fint.graphql.model.model.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelOrganisasjonselementQueryResolver")
@Slf4j
public class OrganisasjonselementQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OrganisasjonselementService service;

    public CompletionStage<OrganisasjonselementResource> organisasjonselement(
            String organisasjonsId,
            String organisasjonsKode,
            String organisasjonsnummer,
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
