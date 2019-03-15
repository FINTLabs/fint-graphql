// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.arbeidsforholdstype.ArbeidsforholdstypeService;
import no.fint.graphql.model.administrasjon.art.ArtService;
import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.stillingskode.StillingskodeService;
import no.fint.graphql.model.administrasjon.uketimetall.UketimetallService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.fastlonn.FastlonnService;
import no.fint.graphql.model.administrasjon.fasttillegg.FasttilleggService;
import no.fint.graphql.model.administrasjon.variabellonn.VariabellonnService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.FastlonnResource;
import no.fint.model.resource.administrasjon.personal.FasttilleggResource;
import no.fint.model.resource.administrasjon.personal.VariabellonnResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonArbeidsforholdResolver")
public class ArbeidsforholdResolver implements GraphQLResolver<ArbeidsforholdResource> {

    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private ArbeidsforholdstypeService arbeidsforholdstypeService;

    @Autowired
    private ArtService artService;

    @Autowired
    private FunksjonService funksjonService;

    @Autowired
    private StillingskodeService stillingskodeService;

    @Autowired
    private UketimetallService uketimetallService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private FastlonnService fastlonnService;

    @Autowired
    private FasttilleggService fasttilleggService;

    @Autowired
    private VariabellonnService variabellonnService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;


    public AnsvarResource getAnsvar(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getAnsvar()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public ArbeidsforholdstypeResource getArbeidsforholdstype(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getArbeidsforholdstype()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdstypeService.getArbeidsforholdstypeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public ArtResource getArt(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getArt()
                .stream()
                .map(Link::getHref)
                .map(l -> artService.getArtResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public FunksjonResource getFunksjon(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getFunksjon()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public StillingskodeResource getStillingskode(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getStillingskode()
                .stream()
                .map(Link::getHref)
                .map(l -> stillingskodeService.getStillingskodeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public UketimetallResource getTimerPerUke(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getTimerPerUke()
                .stream()
                .map(Link::getHref)
                .map(l -> uketimetallService.getUketimetallResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public OrganisasjonselementResource getArbeidssted(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getArbeidssted()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getPersonalleder(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getPersonalleder()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<FastlonnResource> getFastlonn(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getFastlonn()
                .stream()
                .map(Link::getHref)
                .map(l -> fastlonnService.getFastlonnResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<FasttilleggResource> getFasttillegg(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getFasttillegg()
                .stream()
                .map(Link::getHref)
                .map(l -> fasttilleggService.getFasttilleggResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<VariabellonnResource> getVariabellonn(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getVariabellonn()
                .stream()
                .map(Link::getHref)
                .map(l -> variabellonnService.getVariabellonnResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PersonalressursResource getPersonalressurs(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getPersonalressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public UndervisningsforholdResource getUndervisningsforhold(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforhold.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

