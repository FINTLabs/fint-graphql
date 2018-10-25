// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.fravar;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.vurdering.FravarResource;
import no.fint.model.resource.utdanning.vurdering.FravarResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningFravarQueryResolver")
public class FravarQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarService service;

    public List<FravarResource> getFravar(String sinceTimeStamp) {
        FravarResources resources = service.getFravarResources(sinceTimeStamp);
        return resources.getContent();
    }
}
