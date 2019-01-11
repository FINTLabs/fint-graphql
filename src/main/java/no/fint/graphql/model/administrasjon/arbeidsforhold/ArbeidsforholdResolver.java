// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.arbeidsforholdstype.ArbeidsforholdstypeService;
import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.stillingskode.StillingskodeService;
import no.fint.graphql.model.administrasjon.uketimetall.UketimetallService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;


import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;


import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonArbeidsforholdResolver")
public class ArbeidsforholdResolver implements GraphQLResolver<ArbeidsforholdResource> {


    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private ArbeidsforholdstypeService arbeidsforholdstypeService;

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
    private UndervisningsforholdService undervisningsforholdService;


    public AnsvarResource getAnsvar(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return ansvarService.getAnsvarResource(
            Links.get(arbeidsforhold.getAnsvar()),
            dfe);
    }

    public ArbeidsforholdstypeResource getArbeidsforholdstype(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return arbeidsforholdstypeService.getArbeidsforholdstypeResource(
            Links.get(arbeidsforhold.getArbeidsforholdstype()),
            dfe);
    }

    public FunksjonResource getFunksjon(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return funksjonService.getFunksjonResource(
            Links.get(arbeidsforhold.getFunksjon()),
            dfe);
    }

    public StillingskodeResource getStillingskode(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return stillingskodeService.getStillingskodeResource(
            Links.get(arbeidsforhold.getStillingskode()),
            dfe);
    }

    public UketimetallResource getTimerPerUke(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return uketimetallService.getUketimetallResource(
            Links.get(arbeidsforhold.getTimerPerUke()),
            dfe);
    }

    public OrganisasjonselementResource getArbeidssted(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return organisasjonselementService.getOrganisasjonselementResource(
            Links.get(arbeidsforhold.getArbeidssted()),
            dfe);
    }

    public PersonalressursResource getPersonalleder(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return personalressursService.getPersonalressursResource(
            Links.get(arbeidsforhold.getPersonalleder()),
            dfe);
    }

    public PersonalressursResource getPersonalressurs(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return personalressursService.getPersonalressursResource(
            Links.get(arbeidsforhold.getPersonalressurs()),
            dfe);
    }

    public UndervisningsforholdResource getUndervisningsforhold(ArbeidsforholdResource arbeidsforhold, DataFetchingEnvironment dfe) {
        return undervisningsforholdService.getUndervisningsforholdResource(
            Links.get(arbeidsforhold.getUndervisningsforhold()),
            dfe);
    }

}

