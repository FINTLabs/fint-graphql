// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.personalressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.personalressurskategori.PersonalressurskategoriService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.felles.person.PersonService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;


import no.fint.model.resource.administrasjon.personal.PersonalressursResource;


import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonPersonalressursResolver")
public class PersonalressursResolver implements GraphQLResolver<PersonalressursResource> {


    @Autowired
    private PersonalressurskategoriService personalressurskategoriService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private PersonService personService;

    @Autowired
    private FullmaktService fullmaktService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private SkoleressursService skoleressursService;


    public PersonalressurskategoriResource getPersonalressurskategori(PersonalressursResource personalressurs) {
        return personalressurskategoriService.getPersonalressurskategoriResource(Links.get(personalressurs.getPersonalressurskategori()));
    }

    public ArbeidsforholdResource getArbeidsforhold(PersonalressursResource personalressurs) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(personalressurs.getArbeidsforhold()));
    }

    public PersonResource getPerson(PersonalressursResource personalressurs) {
        return personService.getPersonResource(Links.get(personalressurs.getPerson()));
    }

    public FullmaktResource getStedfortreder(PersonalressursResource personalressurs) {
        return fullmaktService.getFullmaktResource(Links.get(personalressurs.getStedfortreder()));
    }

    public FullmaktResource getFullmakt(PersonalressursResource personalressurs) {
        return fullmaktService.getFullmaktResource(Links.get(personalressurs.getFullmakt()));
    }

    public OrganisasjonselementResource getLeder(PersonalressursResource personalressurs) {
        return organisasjonselementService.getOrganisasjonselementResource(Links.get(personalressurs.getLeder()));
    }

    public ArbeidsforholdResource getPersonalansvar(PersonalressursResource personalressurs) {
        return arbeidsforholdService.getArbeidsforholdResource(Links.get(personalressurs.getPersonalansvar()));
    }

    public SkoleressursResource getSkoleressurs(PersonalressursResource personalressurs) {
        return skoleressursService.getSkoleressursResource(Links.get(personalressurs.getSkoleressurs()));
    }

}

