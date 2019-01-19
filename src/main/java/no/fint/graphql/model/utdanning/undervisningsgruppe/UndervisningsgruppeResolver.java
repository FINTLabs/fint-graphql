// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.undervisningsgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.time.TimeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningUndervisningsgruppeResolver")
public class UndervisningsgruppeResolver implements GraphQLResolver<UndervisningsgruppeResource> {

    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public List<FagResource> getFag(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return undervisningsgruppe.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public SkoleResource getSkole(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return undervisningsgruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<ElevforholdResource> getElevforhold(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return undervisningsgruppe.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<UndervisningsforholdResource> getUndervisningsforhold(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return undervisningsgruppe.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<TimeResource> getTime(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return undervisningsgruppe.getTime()
                .stream()
                .map(Link::getHref)
                .map(l -> timeService.getTimeResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<MedlemskapResource> getMedlemskap(UndervisningsgruppeResource undervisningsgruppe, DataFetchingEnvironment dfe) {
        return undervisningsgruppe.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

