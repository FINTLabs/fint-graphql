
package no.fint.graphql.model.administrasjon.ansvar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonAnsvarResolver")
public class AnsvarResolver implements GraphQLResolver<AnsvarResource> {

    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private FullmaktService fullmaktService;


    public CompletionStage<AnsvarResource> getOverordnet(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(ansvar.getOverordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<List<AnsvarResource>> getUnderordnet(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(ansvar.getUnderordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<OrganisasjonselementResource>> getOrganisasjonselement(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(ansvar.getOrganisasjonselement()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<FullmaktResource>> getFullmakt(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(ansvar.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

