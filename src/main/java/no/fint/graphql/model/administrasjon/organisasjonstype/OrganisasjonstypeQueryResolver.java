
package no.fint.graphql.model.administrasjon.organisasjonstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.OrganisasjonstypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonOrganisasjonstypeQueryResolver")
@Slf4j
public class OrganisasjonstypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OrganisasjonstypeService service;

    public CompletionStage<OrganisasjonstypeResource> getOrganisasjonstype(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Organisasjonstype");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getOrganisasjonstypeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<OrganisasjonstypeResource>empty().toFuture();
    }
}
