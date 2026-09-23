
package no.fint.graphql.model.model.rom;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.timeplan.RomResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelRomQueryResolver")
@Slf4j
public class RomQueryResolver {

    @Autowired
    private RomService service;

    @QueryMapping(name = "rom")
    public CompletionStage<RomResource> rom(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Rom");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getRomResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<RomResource>empty().toFuture();
    }
}
