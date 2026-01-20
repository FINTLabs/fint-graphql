
package no.fint.graphql.model.model.organisasjonselement;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.ansvar.AnsvarService;
import no.fint.graphql.model.model.organisasjonstype.OrganisasjonstypeService;
import no.fint.graphql.model.model.personalressurs.PersonalressursService;
import no.fint.graphql.model.model.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.model.skole.SkoleService;
import no.fint.graphql.model.model.arbeidsforhold.ArbeidsforholdService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.OrganisasjonstypeResource;
import no.novari.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.novari.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.novari.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

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
        return Flux.fromStream(organisasjonselement.getAnsvar()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
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
        return Flux.fromStream(organisasjonselement.getUnderordnet()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
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
        return Flux.fromStream(organisasjonselement.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

