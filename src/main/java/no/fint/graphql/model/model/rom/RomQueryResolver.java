
package no.fint.graphql.model.model.rom;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.RomResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelRomQueryResolver")
@Slf4j
public class RomQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private RomService service;

    public CompletionStage<RomResource> getRom(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Rom");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getRomResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<RomResource>empty().toFuture();
    }
}
