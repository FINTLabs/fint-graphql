// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elevkategori;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResource;
import no.fint.model.resource.utdanning.kodeverk.ElevkategoriResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningElevkategoriQueryResolver")
public class ElevkategoriQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevkategoriService service;

    public List<ElevkategoriResource> getElevkategori(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        ElevkategoriResources resources = service.getElevkategoriResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
