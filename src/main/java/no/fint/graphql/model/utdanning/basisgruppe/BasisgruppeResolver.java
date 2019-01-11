// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.basisgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.arstrinn.ArstrinnService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("utdanningBasisgruppeResolver")
public class BasisgruppeResolver implements GraphQLResolver<BasisgruppeResource> {

    @Autowired
    private ArstrinnService arstrinnService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public ArstrinnResource getTrinn(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return basisgruppe.getTrinn()
            .stream()
            .map(Link::getHref)
            .map(l -> arstrinnService.getArstrinnResource(l, dfe))
            .findFirst().orElse(null);
    }

    public SkoleResource getSkole(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return basisgruppe.getSkole()
            .stream()
            .map(Link::getHref)
            .map(l -> skoleService.getSkoleResource(l, dfe))
            .findFirst().orElse(null);
    }

    public List<UndervisningsforholdResource> getUndervisningsforhold(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return basisgruppe.getUndervisningsforhold()
            .stream()
            .map(Link::getHref)
            .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe))
            .collect(Collectors.toList());
    }

    public List<ElevforholdResource> getElevforhold(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return basisgruppe.getElevforhold()
            .stream()
            .map(Link::getHref)
            .map(l -> elevforholdService.getElevforholdResource(l, dfe))
            .collect(Collectors.toList());
    }

    public List<KontaktlarergruppeResource> getKontaktlarergruppe(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return basisgruppe.getKontaktlarergruppe()
            .stream()
            .map(Link::getHref)
            .map(l -> kontaktlarergruppeService.getKontaktlarergruppeResource(l, dfe))
            .collect(Collectors.toList());
    }

    public List<MedlemskapResource> getMedlemskap(BasisgruppeResource basisgruppe, DataFetchingEnvironment dfe) {
        return basisgruppe.getMedlemskap()
            .stream()
            .map(Link::getHref)
            .map(l -> medlemskapService.getMedlemskapResource(l, dfe))
            .collect(Collectors.toList());
    }

}

