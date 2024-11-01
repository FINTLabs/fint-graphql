
package no.fint.graphql.model.administrasjon.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonOrganisasjonselementQueryResolver")
public class OrganisasjonselementQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OrganisasjonselementService service;

    public CompletionStage<OrganisasjonselementResource> getOrganisasjonselement(
            String organisasjonsId,
            String organisasjonsKode,
            String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
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
