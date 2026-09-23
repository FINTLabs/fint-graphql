
package no.fint.graphql.model.model.elevtilrettelegging;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.eksamensform.EksamensformService;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.fag.FagService;
import no.fint.graphql.model.model.tilrettelegging.TilretteleggingService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import no.novari.fint.model.resource.utdanning.kodeverk.EksamensformResource;
import no.novari.fint.model.resource.utdanning.kodeverk.TilretteleggingResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelElevtilretteleggingResolver")
public class ElevtilretteleggingResolver {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private FagService fagService;

    @Autowired
    private TilretteleggingService tilretteleggingService;

    @Autowired
    private EksamensformService eksamensformService;


    @SchemaMapping(typeName = "Elevtilrettelegging", field = "elev")
    public CompletionStage<ElevforholdResource> getElev(ElevtilretteleggingResource elevtilrettelegging, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevtilrettelegging.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Elevtilrettelegging", field = "fag")
    public CompletionStage<FagResource> getFag(ElevtilretteleggingResource elevtilrettelegging, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevtilrettelegging.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Elevtilrettelegging", field = "tilrettelegging")
    public CompletionStage<TilretteleggingResource> getTilrettelegging(ElevtilretteleggingResource elevtilrettelegging, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevtilrettelegging.getTilrettelegging()
                .stream()
                .map(Link::getHref)
                .map(l -> tilretteleggingService.getTilretteleggingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Elevtilrettelegging", field = "eksamensform")
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

