
package no.fint.graphql.model.administrasjon.objekt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.kodeverk.ObjektResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonObjektQueryResolver")
@Slf4j
public class ObjektQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ObjektService service;

    public CompletionStage<ObjektResource> getObjekt(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Objekt");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getObjektResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ObjektResource>empty().toFuture();
    }
}
