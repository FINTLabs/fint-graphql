// Built from tag master

package no.fint.graphql.model.utdanning.programomrade;

import com.coxautodev.graphql.tools.GraphQLResolver;
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


    public UtdanningsprogramResource getUtdanningsprogram(ProgramomradeResource programomrade) {
        return utdanningsprogramService.getUtdanningsprogramResource(Links.get(programomrade.getUtdanningsprogram()));
    }

    public FagResource getFag(ProgramomradeResource programomrade) {
        return fagService.getFagResource(Links.get(programomrade.getFag()));
    }

    public ArstrinnResource getTrinn(ProgramomradeResource programomrade) {
        return arstrinnService.getArstrinnResource(Links.get(programomrade.getTrinn()));
    }

    public MedlemskapResource getMedlemskap(ProgramomradeResource programomrade) {
        return medlemskapService.getMedlemskapResource(Links.get(programomrade.getMedlemskap()));
    }

}

