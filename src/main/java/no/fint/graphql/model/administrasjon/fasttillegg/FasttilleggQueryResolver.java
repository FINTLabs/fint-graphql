// Built from tag master

package no.fint.graphql.model.administrasjon.fasttillegg;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.personal.FasttilleggResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonFasttilleggQueryResolver")
public class FasttilleggQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FasttilleggService service;

    public List<FasttilleggResource> getFasttillegg(String sinceTimeStamp) {
        FasttilleggResources resources = service.getFasttilleggResources(sinceTimeStamp);
        return resources.getContent();
    }
}
