
package no.fint.graphql.model.model.eksamen;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.rom.RomService;
import no.fint.graphql.model.model.eksamensgruppe.EksamensgruppeService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.timeplan.EksamenResource;
import no.novari.fint.model.resource.utdanning.timeplan.RomResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelEksamenResolver")
public class EksamenResolver implements GraphQLResolver<EksamenResource> {

    @Autowired
    private RomService romService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;


    public CompletionStage<List<RomResource>> getRom(EksamenResource eksamen, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamen.getRom()
                .stream()
                .map(Link::getHref)
                .map(l -> romService.getRomResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppeResource>> getEksamensgruppe(EksamenResource eksamen, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamen.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

