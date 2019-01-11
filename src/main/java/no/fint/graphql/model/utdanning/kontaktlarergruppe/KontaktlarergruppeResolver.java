// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.kontaktlarergruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("utdanningKontaktlarergruppeResolver")
public class KontaktlarergruppeResolver implements GraphQLResolver<KontaktlarergruppeResource> {

    @Autowired
    private BasisgruppeService basisgruppeService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private MedlemskapService medlemskapService;


    public List<BasisgruppeResource> getBasisgruppe(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return kontaktlarergruppe.getBasisgruppe()
            .stream()
            .map(Link::getHref)
            .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe))
            .collect(Collectors.toList());
    }

    public SkoleResource getSkole(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return kontaktlarergruppe.getSkole()
            .stream()
            .map(Link::getHref)
            .map(l -> skoleService.getSkoleResource(l, dfe))
            .findFirst().orElse(null);
    }

    public List<ElevforholdResource> getElevforhold(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return kontaktlarergruppe.getElevforhold()
            .stream()
            .map(Link::getHref)
            .map(l -> elevforholdService.getElevforholdResource(l, dfe))
            .collect(Collectors.toList());
    }

    public List<UndervisningsforholdResource> getUndervisningsforhold(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return kontaktlarergruppe.getUndervisningsforhold()
            .stream()
            .map(Link::getHref)
            .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe))
            .collect(Collectors.toList());
    }

    public List<MedlemskapResource> getMedlemskap(KontaktlarergruppeResource kontaktlarergruppe, DataFetchingEnvironment dfe) {
        return kontaktlarergruppe.getMedlemskap()
            .stream()
            .map(Link::getHref)
            .map(l -> medlemskapService.getMedlemskapResource(l, dfe))
            .collect(Collectors.toList());
    }

}

