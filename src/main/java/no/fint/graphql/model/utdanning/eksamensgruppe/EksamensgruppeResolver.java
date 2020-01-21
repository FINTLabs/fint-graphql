
package no.fint.graphql.model.utdanning.eksamensgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningEksamensgruppeResolver")
public class EksamensgruppeResolver implements GraphQLResolver<EksamensgruppeResource> {

    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private MedlemskapService medlemskapService;


    public FagResource getFag(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return eksamensgruppe.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public SkoleResource getSkole(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return eksamensgruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<ElevforholdResource> getElevforhold(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .block();
    }

    public List<UndervisningsforholdResource> getUndervisningsforhold(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return eksamensgruppe.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<MedlemskapResource> getMedlemskap(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return eksamensgruppe.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

