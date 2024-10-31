
package no.fint.graphql.model.utdanning.avlagtprove;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.provestatus.ProvestatusService;
import no.fint.graphql.model.utdanning.larling.LarlingService;
import no.fint.graphql.model.utdanning.fullfortkode.FullfortkodeService;
import no.fint.graphql.model.utdanning.brevtype.BrevtypeService;
import no.fint.graphql.model.utdanning.bevistype.BevistypeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.larling.AvlagtProveResource;
import no.fint.model.resource.utdanning.kodeverk.ProvestatusResource;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fint.model.resource.utdanning.kodeverk.FullfortkodeResource;
import no.fint.model.resource.utdanning.kodeverk.BrevtypeResource;
import no.fint.model.resource.utdanning.kodeverk.BevistypeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningAvlagtProveResolver")
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

