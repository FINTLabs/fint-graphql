
package no.fint.graphql.model.model.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.ansvar.AnsvarService;
import no.fint.graphql.model.model.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.model.organisasjonstype.OrganisasjonstypeService;
import no.fint.graphql.model.model.personalressurs.PersonalressursService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.OrganisasjonstypeResource;
import no.novari.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.novari.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.novari.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
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

@Component("modelOrganisasjonselementResolver")
public class OrganisasjonselementResolver implements GraphQLResolver<OrganisasjonselementResource> {

    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private OrganisasjonstypeService organisasjonstypeService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;


    public CompletionStage<List<AnsvarResource>> getAnsvar(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(organisasjonselement.getAnsvar()).orElseGet(List::of);
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

    public CompletionStage<OrganisasjonstypeResource> getOrganisasjonstype(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return Flux.fromStream(organisasjonselement.getOrganisasjonstype()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonstypeService.getOrganisasjonstypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersonalressursResource> getLeder(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return Flux.fromStream(organisasjonselement.getLeder()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<OrganisasjonselementResource> getOverordnet(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return Flux.fromStream(organisasjonselement.getOverordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<OrganisasjonselementResource>> getUnderordnet(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(organisasjonselement.getUnderordnet()).orElseGet(List::of);
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

    public CompletionStage<SkoleResource> getSkole(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        return Flux.fromStream(organisasjonselement.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<ArbeidsforholdResource>> getArbeidsforhold(OrganisasjonselementResource organisasjonselement, DataFetchingEnvironment dfe) {
        var links = Optional.ofNullable(organisasjonselement.getArbeidsforhold()).orElseGet(List::of);
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

