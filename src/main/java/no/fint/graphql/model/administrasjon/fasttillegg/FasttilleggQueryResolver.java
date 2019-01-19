// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fasttillegg;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.personal.FasttilleggResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonFasttilleggQueryResolver")
public class FasttilleggQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FasttilleggService service;

    public List<FasttilleggResource> getFasttillegg(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        FasttilleggResources resources = service.getFasttilleggResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
