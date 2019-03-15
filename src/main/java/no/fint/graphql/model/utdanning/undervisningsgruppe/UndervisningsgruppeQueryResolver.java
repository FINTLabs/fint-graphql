// Built from tag release-3.2

package no.fint.graphql.model.utdanning.undervisningsgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningUndervisningsgruppeQueryResolver")
public class UndervisningsgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UndervisningsgruppeService service;

    public UndervisningsgruppeResource getUndervisningsgruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUndervisningsgruppeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
