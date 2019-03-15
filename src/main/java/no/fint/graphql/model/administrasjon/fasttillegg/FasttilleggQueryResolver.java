
package no.fint.graphql.model.administrasjon.fasttillegg;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFasttilleggQueryResolver")
public class FasttilleggQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private FasttilleggService service;

    public FasttilleggResource getFasttillegg(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getFasttilleggResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
