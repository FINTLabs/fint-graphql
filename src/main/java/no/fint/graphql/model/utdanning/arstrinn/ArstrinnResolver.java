// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.arstrinn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("utdanningArstrinnResolver")
public class ArstrinnResolver implements GraphQLResolver<ArstrinnResource> {

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private BasisgruppeService basisgruppeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public List<ProgramomradeResource> getProgramomrade(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return arstrinn.getProgramomrade()
            .stream()
            .map(Link::getHref)
            .map(l -> programomradeService.getProgramomradeResource(l, dfe))
            .collect(Collectors.toList());
    }

    public List<BasisgruppeResource> getBasisgruppe(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return arstrinn.getBasisgruppe()
            .stream()
            .map(Link::getHref)
            .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe))
            .collect(Collectors.toList());
    }

    public List<MedlemskapResource> getMedlemskap(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return arstrinn.getMedlemskap()
            .stream()
            .map(Link::getHref)
            .map(l -> medlemskapService.getMedlemskapResource(l, dfe))
            .collect(Collectors.toList());
    }

}

