
package no.fint.graphql.model.utdanning.undervisningsforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningUndervisningsforholdQueryResolver")
public class UndervisningsforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UndervisningsforholdService service;

    public CompletionStage<UndervisningsforholdResource> getUndervisningsforhold(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUndervisningsforholdResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<UndervisningsforholdResource>empty().toFuture();
    }
}
