
package no.fint.graphql.model.utdanning.rom;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.time.TimeService;
import no.fint.graphql.model.utdanning.eksamen.EksamenService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.RomResource;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.timeplan.EksamenResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningRomResolver")
public class RomResolver implements GraphQLResolver<RomResource> {

    @Autowired
    private TimeService timeService;

    @Autowired
    private EksamenService eksamenService;


    public CompletionStage<List<TimeResource>> getTime(RomResource rom, DataFetchingEnvironment dfe) {
        return Flux.fromStream(rom.getTime()
                .stream()
                .map(Link::getHref)
                .map(l -> timeService.getTimeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<EksamenResource>> getEksamen(RomResource rom, DataFetchingEnvironment dfe) {
        return Flux.fromStream(rom.getEksamen()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamenService.getEksamenResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

