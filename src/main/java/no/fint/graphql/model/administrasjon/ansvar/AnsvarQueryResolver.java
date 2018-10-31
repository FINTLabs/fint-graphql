// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.ansvar;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonAnsvarQueryResolver")
public class AnsvarQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AnsvarService service;

    public List<AnsvarResource> getAnsvar(String sinceTimeStamp) {
        AnsvarResources resources = service.getAnsvarResources(sinceTimeStamp);
        return resources.getContent();
    }
}
