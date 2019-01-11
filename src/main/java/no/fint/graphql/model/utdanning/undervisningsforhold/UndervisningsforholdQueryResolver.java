// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningUndervisningsforholdQueryResolver")
public class UndervisningsforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UndervisningsforholdService service;

    public List<UndervisningsforholdResource> getUndervisningsforhold(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        UndervisningsforholdResources resources = service.getUndervisningsforholdResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
