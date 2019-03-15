
package no.fint.graphql.model.administrasjon.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonArbeidsforholdQueryResolver")
public class ArbeidsforholdQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArbeidsforholdService service;

    public ArbeidsforholdResource getArbeidsforhold(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArbeidsforholdResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
