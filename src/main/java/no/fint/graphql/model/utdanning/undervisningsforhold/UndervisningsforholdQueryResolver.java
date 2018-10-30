// Built from tag master

package no.fint.graphql.model.utdanning.undervisningsforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningUndervisningsforholdQueryResolver")
public class UndervisningsforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UndervisningsforholdService service;

    public List<UndervisningsforholdResource> getUndervisningsforhold(String sinceTimeStamp) {
        UndervisningsforholdResources resources = service.getUndervisningsforholdResources(sinceTimeStamp);
        return resources.getContent();
    }
}
