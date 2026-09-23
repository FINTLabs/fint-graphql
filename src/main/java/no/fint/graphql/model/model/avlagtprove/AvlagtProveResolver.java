
package no.fint.graphql.model.model.avlagtprove;

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
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelAvlagtProveResolver")
public class AvlagtProveResolver {

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


    @SchemaMapping(typeName = "AvlagtProve", field = "provestatus")
    public CompletionStage<ProvestatusResource> getProvestatus(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getProvestatus()
                .stream()
                .map(Link::getHref)
                .map(l -> provestatusService.getProvestatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "AvlagtProve", field = "larling")
    public CompletionStage<LarlingResource> getLarling(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getLarling()
                .stream()
                .map(Link::getHref)
                .map(l -> larlingService.getLarlingResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "AvlagtProve", field = "fullfortkode")
    public CompletionStage<FullfortkodeResource> getFullfortkode(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getFullfortkode()
                .stream()
                .map(Link::getHref)
                .map(l -> fullfortkodeService.getFullfortkodeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "AvlagtProve", field = "brevtype")
    public CompletionStage<BrevtypeResource> getBrevtype(AvlagtProveResource avlagtprove, DataFetchingEnvironment dfe) {
        return Flux.fromStream(avlagtprove.getBrevtype()
                .stream()
                .map(Link::getHref)
                .map(l -> brevtypeService.getBrevtypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "AvlagtProve", field = "bevistype")
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

