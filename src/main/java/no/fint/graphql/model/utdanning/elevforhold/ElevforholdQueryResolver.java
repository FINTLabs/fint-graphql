
package no.fint.graphql.model.utdanning.elevforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningElevforholdQueryResolver")
public class ElevforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevforholdService service;

    public CompletionStage<ElevforholdResource> getElevforhold(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevforholdResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<ElevforholdResource>empty().toFuture();
    }
}
