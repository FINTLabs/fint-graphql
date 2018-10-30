// Built from tag master

package no.fint.graphql.model.utdanning.arstrinn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;


import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningArstrinnResolver")
public class ArstrinnResolver implements GraphQLResolver<ArstrinnResource> {


    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private BasisgruppeService basisgruppeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public ProgramomradeResource getProgramomrade(ArstrinnResource arstrinn) {
        return programomradeService.getProgramomradeResource(Links.get(arstrinn.getProgramomrade()));
    }

    public BasisgruppeResource getBasisgruppe(ArstrinnResource arstrinn) {
        return basisgruppeService.getBasisgruppeResource(Links.get(arstrinn.getBasisgruppe()));
    }

    public MedlemskapResource getMedlemskap(ArstrinnResource arstrinn) {
        return medlemskapService.getMedlemskapResource(Links.get(arstrinn.getMedlemskap()));
    }

}

