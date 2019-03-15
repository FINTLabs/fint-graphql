// Built from tag release-3.2

package no.fint.graphql.model.utdanning.fravar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.fravarstype.FravarstypeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.FravarResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.kodeverk.FravarstypeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningFravarResolver")
public class FravarResolver implements GraphQLResolver<FravarResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private FravarstypeService fravarstypeService;


    public ElevforholdResource getElevforhold(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public UndervisningsgruppeResource getUndervisningsgruppe(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public EksamensgruppeResource getEksamensgruppe(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public FravarstypeResource getFravarstype(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getFravarstype()
                .stream()
                .map(Link::getHref)
                .map(l -> fravarstypeService.getFravarstypeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

