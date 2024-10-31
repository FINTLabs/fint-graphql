
package no.fint.graphql.model.utdanning.elevtilrettelegging;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.tilrettelegging.TilretteleggingService;
import no.fint.graphql.model.utdanning.eksamensform.EksamensformService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.kodeverk.TilretteleggingResource;
import no.fint.model.resource.utdanning.kodeverk.EksamensformResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningElevtilretteleggingResolver")
public class ElevtilretteleggingResolver implements GraphQLResolver<ElevtilretteleggingResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private FagService fagService;

    @Autowired
    private TilretteleggingService tilretteleggingService;

    @Autowired
    private EksamensformService eksamensformService;


    public CompletionStage<ElevforholdResource> getElev(ElevtilretteleggingResource elevtilrettelegging, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevtilrettelegging.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FagResource> getFag(ElevtilretteleggingResource elevtilrettelegging, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevtilrettelegging.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<TilretteleggingResource> getTilrettelegging(ElevtilretteleggingResource elevtilrettelegging, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevtilrettelegging.getTilrettelegging()
                .stream()
                .map(Link::getHref)
                .map(l -> tilretteleggingService.getTilretteleggingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<EksamensformResource> getEksamensform(ElevtilretteleggingResource elevtilrettelegging, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevtilrettelegging.getEksamensform()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensformService.getEksamensformResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

