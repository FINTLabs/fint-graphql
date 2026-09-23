
package no.fint.graphql.model.model.ansvar;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAnsvarQueryResolver")
@Slf4j
public class AnsvarQueryResolver {

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
