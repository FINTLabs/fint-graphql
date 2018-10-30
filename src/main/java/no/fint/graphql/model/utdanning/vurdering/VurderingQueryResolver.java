// Built from tag master

package no.fint.graphql.model.utdanning.vurdering;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningVurderingQueryResolver")
public class VurderingQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private VurderingService service;

    public List<VurderingResource> getVurdering(String sinceTimeStamp) {
        VurderingResources resources = service.getVurderingResources(sinceTimeStamp);
        return resources.getContent();
    }
}
