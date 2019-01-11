// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fullmakt;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonFullmaktQueryResolver")
public class FullmaktQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FullmaktService service;

    public List<FullmaktResource> getFullmakt(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        FullmaktResources resources = service.getFullmaktResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
