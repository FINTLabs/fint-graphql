// Built from tag v3.1.0

package no.fint.graphql.model.felles.person;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.landkode.LandkodeService;
import no.fint.graphql.model.felles.kjonn.KjonnService;
import no.fint.graphql.model.felles.sprak.SprakService;
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.felles.kontaktperson.KontaktpersonService;
import no.fint.graphql.model.utdanning.elev.ElevService;


import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.kodeverk.iso.LandkodeResource;
import no.fint.model.resource.felles.kodeverk.iso.KjonnResource;
import no.fint.model.resource.felles.kodeverk.iso.SprakResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.KontaktpersonResource;
import no.fint.model.resource.utdanning.elev.ElevResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("fellesPersonResolver")
public class PersonResolver implements GraphQLResolver<PersonResource> {

    @Autowired
    private LandkodeService landkodeService;

    @Autowired
    private KjonnService kjonnService;

    @Autowired
    private SprakService sprakService;

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private KontaktpersonService kontaktpersonService;

    @Autowired
    private ElevService elevService;


    public List<LandkodeResource> getStatsborgerskap(PersonResource person, DataFetchingEnvironment dfe) {
        return person.getStatsborgerskap()
                .stream()
                .map(Link::getHref)
                .map(l -> landkodeService.getLandkodeResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public KjonnResource getKjonn(PersonResource person, DataFetchingEnvironment dfe) {
        return person.getKjonn()
                .stream()
                .map(Link::getHref)
                .map(l -> kjonnService.getKjonnResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public SprakResource getMalform(PersonResource person, DataFetchingEnvironment dfe) {
        return person.getMalform()
                .stream()
                .map(Link::getHref)
                .map(l -> sprakService.getSprakResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public PersonalressursResource getPersonalressurs(PersonResource person, DataFetchingEnvironment dfe) {
        return person.getPersonalressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public SprakResource getMorsmal(PersonResource person, DataFetchingEnvironment dfe) {
        return person.getMorsmal()
                .stream()
                .map(Link::getHref)
                .map(l -> sprakService.getSprakResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<KontaktpersonResource> getParorende(PersonResource person, DataFetchingEnvironment dfe) {
        return person.getParorende()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktpersonService.getKontaktpersonResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ElevResource getElev(PersonResource person, DataFetchingEnvironment dfe) {
        return person.getElev()
                .stream()
                .map(Link::getHref)
                .map(l -> elevService.getElevResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

