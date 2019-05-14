
package no.fint.graphql.model.utdanning.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.skole.SkoleService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningSkoleressursResolver")
public class SkoleressursResolver implements GraphQLResolver<SkoleressursResource> {

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private SkoleService skoleService;


    public PersonalressursResource getPersonalressurs(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return skoleressurs.getPersonalressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<UndervisningsforholdResource> getUndervisningsforhold(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return skoleressurs.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<SkoleResource> getSkole(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return skoleressurs.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

