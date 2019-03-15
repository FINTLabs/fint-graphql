// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.personalressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.personalressurskategori.PersonalressurskategoriService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.felles.person.PersonService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


    public PersonalressurskategoriResource getPersonalressurskategori(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return personalressurs.getPersonalressurskategori()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressurskategoriService.getPersonalressurskategoriResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<ArbeidsforholdResource> getArbeidsforhold(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return personalressurs.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PersonResource getPerson(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return personalressurs.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<FullmaktResource> getStedfortreder(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return personalressurs.getStedfortreder()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<FullmaktResource> getFullmakt(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return personalressurs.getFullmakt()
                .stream()
                .map(Link::getHref)
                .map(l -> fullmaktService.getFullmaktResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<OrganisasjonselementResource> getLeder(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return personalressurs.getLeder()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<ArbeidsforholdResource> getPersonalansvar(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return personalressurs.getPersonalansvar()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public SkoleressursResource getSkoleressurs(PersonalressursResource personalressurs, DataFetchingEnvironment dfe) {
        return personalressurs.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

