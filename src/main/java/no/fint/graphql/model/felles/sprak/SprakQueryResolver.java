// Built from tag v3.1.0

package no.fint.graphql.model.felles.sprak;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import no.fint.model.resource.felles.kodeverk.iso.SprakResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SprakQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SprakService service;

    public List<SprakResource> getSprak(String sinceTimeStamp) {
        SprakResources resources = service.getSprakResources(sinceTimeStamp);
        return resources.getContent();
    }
}
