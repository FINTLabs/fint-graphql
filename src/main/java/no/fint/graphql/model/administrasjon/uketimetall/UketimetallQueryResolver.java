// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.uketimetall;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonUketimetallQueryResolver")
public class UketimetallQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UketimetallService service;

    public UketimetallResource getUketimetall(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getUketimetallResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
