
package no.fint.graphql.model.model.eksamensgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelEksamensgruppeQueryResolver")
@Slf4j
public class EksamensgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private EksamensgruppeService service;

    public CompletionStage<EksamensgruppeResource> getEksamensgruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Eksamensgruppe");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamensgruppeResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamensgruppeResource>empty().toFuture();
    }
}
