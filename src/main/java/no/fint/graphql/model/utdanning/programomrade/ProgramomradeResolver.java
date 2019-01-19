// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.programomrade;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.utdanningsprogram.UtdanningsprogramService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.arstrinn.ArstrinnService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;


import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningProgramomradeResolver")
public class ProgramomradeResolver implements GraphQLResolver<ProgramomradeResource> {


    @Autowired
    private UtdanningsprogramService utdanningsprogramService;

    @Autowired
    private FagService fagService;

    @Autowired
    private ArstrinnService arstrinnService;

    @Autowired
    private MedlemskapService medlemskapService;


    public UtdanningsprogramResource getUtdanningsprogram(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return utdanningsprogramService.getUtdanningsprogramResource(
            Links.get(programomrade.getUtdanningsprogram()),
            dfe);
    }

    public FagResource getFag(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return fagService.getFagResource(
            Links.get(programomrade.getFag()),
            dfe);
    }

    public ArstrinnResource getTrinn(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return arstrinnService.getArstrinnResource(
            Links.get(programomrade.getTrinn()),
            dfe);
    }

    public MedlemskapResource getMedlemskap(ProgramomradeResource programomrade, DataFetchingEnvironment dfe) {
        return medlemskapService.getMedlemskapResource(
            Links.get(programomrade.getMedlemskap()),
            dfe);
    }

}

