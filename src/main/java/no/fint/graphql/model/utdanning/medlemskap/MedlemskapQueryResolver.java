// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.medlemskap;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningMedlemskapQueryResolver")
public class MedlemskapQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private MedlemskapService service;

    public List<MedlemskapResource> getMedlemskap(String sinceTimeStamp) {
        MedlemskapResources resources = service.getMedlemskapResources(sinceTimeStamp);
        return resources.getContent();
    }
}
