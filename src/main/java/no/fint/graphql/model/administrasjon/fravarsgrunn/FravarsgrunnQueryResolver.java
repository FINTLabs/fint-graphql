// Built from tag master

package no.fint.graphql.model.administrasjon.fravarsgrunn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonFravarsgrunnQueryResolver")
public class FravarsgrunnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarsgrunnService service;

    public List<FravarsgrunnResource> getFravarsgrunn(String sinceTimeStamp) {
        FravarsgrunnResources resources = service.getFravarsgrunnResources(sinceTimeStamp);
        return resources.getContent();
    }
}
