
package no.fint.graphql.model.model.avlagtprove;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.bevistype.BevistypeService;
import no.fint.graphql.model.model.brevtype.BrevtypeService;
import no.fint.graphql.model.model.fullfortkode.FullfortkodeService;
import no.fint.graphql.model.model.larling.LarlingService;
import no.fint.graphql.model.model.provestatus.ProvestatusService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.kodeverk.BevistypeResource;
import no.novari.fint.model.resource.utdanning.kodeverk.BrevtypeResource;
import no.novari.fint.model.resource.utdanning.kodeverk.FullfortkodeResource;
import no.novari.fint.model.resource.utdanning.kodeverk.ProvestatusResource;
import no.novari.fint.model.resource.utdanning.larling.AvlagtProveResource;
import no.novari.fint.model.resource.utdanning.larling.LarlingResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelAvlagtProveResolver")
public class AvlagtProveResolver implements GraphQLResolver<AvlagtProveResource> {

    @Autowired
    private ProvestatusService provestatusService;

    @Autowired
    private LarlingService larlingService;

    @Autowired
    private FullfortkodeService fullfortkodeService;

    @Autowired
    private BrevtypeService brevtypeService;

    @Autowired
    private BevistypeService bevistypeService;


    public CompletionStage<ProvestatusResource> getProvestatus(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getProvestatus()
                .stream()
                .map(Link::getHref)
                .map(l -> provestatusService.getProvestatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<LarlingResource> getLarling(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getLarling()
                .stream()
                .map(Link::getHref)
                .map(l -> larlingService.getLarlingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FullfortkodeResource> getFullfortkode(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getFullfortkode()
                .stream()
                .map(Link::getHref)
                .map(l -> fullfortkodeService.getFullfortkodeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<BrevtypeResource> getBrevtype(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getBrevtype()
                .stream()
                .map(Link::getHref)
                .map(l -> brevtypeService.getBrevtypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<BevistypeResource> getBevistype(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getBevistype()
                .stream()
                .map(Link::getHref)
                .map(l -> bevistypeService.getBevistypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

