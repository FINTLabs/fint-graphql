// Built from tag master

package no.fint.graphql.model.administrasjon.fravar;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.personal.FravarResource;
import no.fint.model.resource.administrasjon.personal.FravarResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonFravarQueryResolver")
public class FravarQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarService service;

    public List<FravarResource> getFravar(String sinceTimeStamp) {
        FravarResources resources = service.getFravarResources(sinceTimeStamp);
        return resources.getContent();
    }
}
