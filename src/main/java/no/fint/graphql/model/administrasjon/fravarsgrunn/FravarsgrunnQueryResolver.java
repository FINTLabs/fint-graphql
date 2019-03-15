// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fravarsgrunn;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFravarsgrunnQueryResolver")
public class FravarsgrunnQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FravarsgrunnService service;

    public FravarsgrunnResource getFravarsgrunn(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFravarsgrunnResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
