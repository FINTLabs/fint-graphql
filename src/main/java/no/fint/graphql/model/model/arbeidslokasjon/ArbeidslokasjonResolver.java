
package no.fint.graphql.model.model.arbeidslokasjon;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.arbeidsforhold.ArbeidsforholdService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.organisasjon.ArbeidslokasjonResource;
import no.novari.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
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

@Component("modelArbeidslokasjonResolver")
public class ArbeidslokasjonResolver implements GraphQLResolver<ArbeidslokasjonResource> {

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;


    public CompletionStage<List<ArbeidsforholdResource>> getArbeidsforhold(ArbeidslokasjonResource arbeidslokasjon, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(arbeidslokasjon.getArbeidsforhold()).orElseGet(List::of);
        if (links.isEmpty()) {
            return CompletableFuture.completedFuture(List.of());
        }
        return Flux.fromIterable(links)
                .map(Link::getHref)
                .flatMapSequential(href -> arbeidsforholdService.getArbeidsforholdResource(href, dfe)
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

