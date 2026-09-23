
package no.fint.graphql.model.model.fravarsoversikt;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.fag.FagService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.timeplan.FagResource;
import no.novari.fint.model.resource.utdanning.vurdering.FravarsoversiktResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelFravarsoversiktResolver")
public class FravarsoversiktResolver {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private FagService fagService;


    @SchemaMapping(typeName = "Fravarsoversikt", field = "elevforhold")
    public CompletionStage<ElevforholdResource> getElevforhold(FravarsoversiktResource fravarsoversikt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsoversikt.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Fravarsoversikt", field = "fag")
    public CompletionStage<FagResource> getFag(FravarsoversiktResource fravarsoversikt, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fravarsoversikt.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

