// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonArbeidsforholdQueryResolver")
public class ArbeidsforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArbeidsforholdService service;

    public List<ArbeidsforholdResource> getArbeidsforhold(String sinceTimeStamp) {
        ArbeidsforholdResources resources = service.getArbeidsforholdResources(sinceTimeStamp);
        return resources.getContent();
    }
}
