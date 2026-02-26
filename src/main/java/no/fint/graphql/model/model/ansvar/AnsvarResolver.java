
package no.fint.graphql.model.model.ansvar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.organisasjonselement.OrganisasjonselementService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.novari.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Component("modelAnsvarResolver")
public class AnsvarResolver implements GraphQLResolver<AnsvarResource> {

    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;


    public CompletionStage<AnsvarResource> getOverordnet(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return Flux.fromStream(ansvar.getOverordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<AnsvarResource>> getUnderordnet(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(ansvar.getUnderordnet()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> ansvarService.getAnsvarResource(href, dfe)
                        .map(Optional::of)
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }

    public CompletionStage<List<OrganisasjonselementResource>> getOrganisasjonselement(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(ansvar.getOrganisasjonselement()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> organisasjonselementService.getOrganisasjonselementResource(href, dfe)
                        .map(Optional::of)
                        .onErrorResume(WebClientResponseException.class,
                                ex -> Mono.just(Optional.empty())),
                        8, 1)
                .collectList()
                .map(list -> list.stream()
                        .map(opt -> opt.orElse(null))
                        .collect(Collectors.toList()))
                .toFuture();
    }

}

