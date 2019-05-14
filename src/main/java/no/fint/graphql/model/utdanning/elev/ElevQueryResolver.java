
package no.fint.graphql.model.utdanning.elev;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.elev.ElevResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningElevQueryResolver")
public class ElevQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ElevService service;

    public ElevResource getElev(
            String brukernavn,
            String elevnummer,
            String feidenavn,
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(brukernavn)) {
            return service.getElevResourceById("brukernavn", brukernavn, dfe);
        }
        if (StringUtils.isNotEmpty(elevnummer)) {
            return service.getElevResourceById("elevnummer", elevnummer, dfe);
        }
        if (StringUtils.isNotEmpty(feidenavn)) {
            return service.getElevResourceById("feidenavn", feidenavn, dfe);
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getElevResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
