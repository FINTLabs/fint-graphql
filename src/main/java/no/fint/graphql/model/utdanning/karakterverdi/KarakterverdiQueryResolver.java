// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterverdi;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource;
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningKarakterverdiQueryResolver")
public class KarakterverdiQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterverdiService service;

    public List<KarakterverdiResource> getKarakterverdi(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        KarakterverdiResources resources = service.getKarakterverdiResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
