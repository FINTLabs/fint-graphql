// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.rom;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.time.TimeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.RomResource;
import no.fint.model.resource.utdanning.timeplan.TimeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningRomResolver")
public class RomResolver implements GraphQLResolver<RomResource> {

    @Autowired
    private TimeService timeService;


    public List<TimeResource> getTime(RomResource rom, DataFetchingEnvironment dfe) {
        return rom.getTime()
                .stream()
                .map(Link::getHref)
                .map(l -> timeService.getTimeResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

