
package no.fint.graphql.model.model.ansvar;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelAnsvarQueryResolver")
@Slf4j
public class AnsvarQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AnsvarService service;

    public CompletionStage<AnsvarResource> ansvar(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Ansvar");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAnsvarResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<AnsvarResource>empty().toFuture();
    }
}
