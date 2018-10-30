// Built from tag master

package no.fint.graphql.model.administrasjon.funksjon;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonFunksjonQueryResolver")
public class FunksjonQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FunksjonService service;

    public List<FunksjonResource> getFunksjon(String sinceTimeStamp) {
        FunksjonResources resources = service.getFunksjonResources(sinceTimeStamp);
        return resources.getContent();
    }
}
