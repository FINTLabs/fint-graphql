
package no.fint.graphql.model.administrasjon.objekt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.ObjektResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonObjektQueryResolver")
public class ObjektQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ObjektService service;

    public CompletionStage<ObjektResource> getObjekt(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getObjektResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ObjektResource>empty().toFuture();
    }
}
