
package no.fint.graphql.model.utdanning.time;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.rom.RomService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.timeplan.RomResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningTimeResolver")
public class TimeResolver implements GraphQLResolver<TimeResource> {

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private RomService romService;


    public CompletionStage<List<UndervisningsgruppeResource>> getUndervisningsgruppe(TimeResource time, DataFetchingEnvironment dfe) {
        return Flux.fromStream(time.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(TimeResource time, DataFetchingEnvironment dfe) {
        return Flux.fromStream(time.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<RomResource>> getRom(TimeResource time, DataFetchingEnvironment dfe) {
        return Flux.fromStream(time.getRom()
                .stream()
                .map(Link::getHref)
                .map(l -> romService.getRomResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

