// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.vurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningVurderingQueryResolver")
public class VurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VurderingService service;

    public List<VurderingResource> getVurdering(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        VurderingResources resources = service.getVurderingResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
