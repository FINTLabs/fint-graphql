// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skole;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningSkoleQueryResolver")
public class SkoleQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SkoleService service;

    public SkoleResource getSkole(
            String skolenummer,
            String systemId,
            String organisasjonsnummer,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(skolenummer)) {
            return service.getSkoleResourceById("skolenummer", skolenummer, dfe);
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSkoleResourceById("systemid", systemId, dfe);
        }
        if (StringUtils.isNotEmpty(organisasjonsnummer)) {
            return service.getSkoleResourceById("organisasjonsnummer", organisasjonsnummer, dfe);
        }
        return null;
    }
}
