// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.art;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonArtQueryResolver")
public class ArtQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArtService service;

    public List<ArtResource> getArt(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        ArtResources resources = service.getArtResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
