// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.karakterskala;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningKarakterskalaQueryResolver")
public class KarakterskalaQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterskalaService service;

    public List<KarakterskalaResource> getKarakterskala(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        KarakterskalaResources resources = service.getKarakterskalaResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
