// Built from tag release-3.2

package no.fint.graphql.model.utdanning.karakterskala;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.kodeverk.KarakterskalaResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningKarakterskalaQueryResolver")
public class KarakterskalaQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private KarakterskalaService service;

    public KarakterskalaResource getKarakterskala(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getKarakterskalaResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
