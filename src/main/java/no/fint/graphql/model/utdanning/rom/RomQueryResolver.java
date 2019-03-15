// Built from tag release-3.2

package no.fint.graphql.model.utdanning.rom;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.utdanning.timeplan.RomResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningRomQueryResolver")
public class RomQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private RomService service;

    public RomResource getRom(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getRomResourceById("systemid", systemId, dfe);
        }
        return null;
    }
}
