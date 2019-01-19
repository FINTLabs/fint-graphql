// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningUndervisningsforholdQueryResolver")
public class UndervisningsforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UndervisningsforholdService service;

    public UndervisningsforholdResource getUndervisningsforhold(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUndervisningsforholdResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
