// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.ansvar;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonAnsvarQueryResolver")
public class AnsvarQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AnsvarService service;

    public AnsvarResource getAnsvar(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getAnsvarResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
