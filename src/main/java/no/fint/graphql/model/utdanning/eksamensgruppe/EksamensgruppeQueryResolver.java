// Built from tag release-3.2

package no.fint.graphql.model.utdanning.eksamensgruppe;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningEksamensgruppeQueryResolver")
public class EksamensgruppeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private EksamensgruppeService service;

    public EksamensgruppeResource getEksamensgruppe(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getEksamensgruppeResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
