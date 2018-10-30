// Built from tag master

package no.fint.graphql.model.administrasjon.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.arbeidsforholdstype.ArbeidsforholdstypeService;
import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.stillingskode.StillingskodeService;
import no.fint.graphql.model.administrasjon.uketimetall.UketimetallService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.fravar.FravarService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;


import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;


import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.StillingskodeResource;
import no.fint.model.resource.administrasjon.kodeverk.UketimetallResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.FravarResource;
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
    private FravarService fravarService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;


    public AnsvarResource getAnsvar(ArbeidsforholdResource arbeidsforhold) {
        return ansvarService.getAnsvarResource(Links.get(arbeidsforhold.getAnsvar()));
    }

    public ArbeidsforholdstypeResource getArbeidsforholdstype(ArbeidsforholdResource arbeidsforhold) {
        return arbeidsforholdstypeService.getArbeidsforholdstypeResource(Links.get(arbeidsforhold.getArbeidsforholdstype()));
    }

    public FunksjonResource getFunksjon(ArbeidsforholdResource arbeidsforhold) {
        return funksjonService.getFunksjonResource(Links.get(arbeidsforhold.getFunksjon()));
    }

    public StillingskodeResource getStillingskode(ArbeidsforholdResource arbeidsforhold) {
        return stillingskodeService.getStillingskodeResource(Links.get(arbeidsforhold.getStillingskode()));
    }

    public UketimetallResource getTimerPerUke(ArbeidsforholdResource arbeidsforhold) {
        return uketimetallService.getUketimetallResource(Links.get(arbeidsforhold.getTimerPerUke()));
    }

    public OrganisasjonselementResource getArbeidssted(ArbeidsforholdResource arbeidsforhold) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(arbeidsforhold.getArbeidssted()));
    }

    public PersonalressursResource getPersonalleder(ArbeidsforholdResource arbeidsforhold) {
        return personalressursService.getPersonalressursResource(Links.get(arbeidsforhold.getPersonalleder()));
    }

    public FravarResource getFravar(ArbeidsforholdResource arbeidsforhold) {
        return fravarService.getFravarResource(Links.get(arbeidsforhold.getFravar()));
    }

    public PersonalressursResource getPersonalressurs(ArbeidsforholdResource arbeidsforhold) {
        return personalressursService.getPersonalressursResource(Links.get(arbeidsforhold.getPersonalressurs()));
    }

    public UndervisningsforholdResource getUndervisningsforhold(ArbeidsforholdResource arbeidsforhold) {
        return undervisningsforholdService.getUndervisningsforholdResource(Links.get(arbeidsforhold.getUndervisningsforhold()));
    }

}

