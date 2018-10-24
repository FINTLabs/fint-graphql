// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.art;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArtQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArtService service;

    public List<ArtResource> getArt(String sinceTimeStamp) {
        ArtResources resources = service.getArtResources(sinceTimeStamp);
        return resources.getContent();
    }
}
