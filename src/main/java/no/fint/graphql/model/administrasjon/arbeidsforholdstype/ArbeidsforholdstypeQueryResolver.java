// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.arbeidsforholdstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonArbeidsforholdstypeQueryResolver")
public class ArbeidsforholdstypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArbeidsforholdstypeService service;

    public ArbeidsforholdstypeResource getArbeidsforholdstype(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getArbeidsforholdstypeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
