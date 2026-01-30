package model.Elevforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import model.Elev.ElevService;
import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningElevforholdResolver")
public class ElevforholdResolver implements GraphQLResolver<ElevforholdResource> {

    @Autowired
    private ElevService elevService;

    public CompletionStage<ElevResource> getElev(ElevforholdResource elevforhold, DataFetchingEnvironment dfe) {
        return Flux.fromStream(elevforhold.getElev().stream().map(Link::getHref).map(l -> elevService.getElevResource(l, dfe))).flatMap(Mono::flux).next().toFuture();
    }
}

