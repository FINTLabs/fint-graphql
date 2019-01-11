// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.arstrinn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
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


    public ProgramomradeResource getProgramomrade(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return programomradeService.getProgramomradeResource(
            Links.get(arstrinn.getProgramomrade()),
            dfe);
    }

    public BasisgruppeResource getBasisgruppe(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return basisgruppeService.getBasisgruppeResource(
            Links.get(arstrinn.getBasisgruppe()),
            dfe);
    }

    public MedlemskapResource getMedlemskap(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return medlemskapService.getMedlemskapResource(
            Links.get(arstrinn.getMedlemskap()),
            dfe);
    }

}

