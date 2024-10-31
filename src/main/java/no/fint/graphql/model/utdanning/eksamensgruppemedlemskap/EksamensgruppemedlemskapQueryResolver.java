
package no.fint.graphql.model.utdanning.eksamensgruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningEksamensgruppemedlemskapQueryResolver")
public class EksamensgruppemedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private EksamensgruppemedlemskapService service;

    public CompletionStage<EksamensgruppemedlemskapResource> getEksamensgruppemedlemskap(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamensgruppemedlemskapResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<EksamensgruppemedlemskapResource>empty().toFuture();
    }
}
