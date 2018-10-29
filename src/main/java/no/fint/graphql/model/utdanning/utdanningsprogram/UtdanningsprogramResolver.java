// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.utdanningsprogram;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;


import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningUtdanningsprogramResolver")
public class UtdanningsprogramResolver implements GraphQLResolver<UtdanningsprogramResource> {


    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public SkoleResource getSkole(UtdanningsprogramResource utdanningsprogram) {
        return skoleService.getSkoleResource(Links.get(utdanningsprogram, "skole"));
    }

    public ProgramomradeResource getProgramomrade(UtdanningsprogramResource utdanningsprogram) {
        return programomradeService.getProgramomradeResource(Links.get(utdanningsprogram, "programomrade"));
    }

    public MedlemskapResource getMedlemskap(UtdanningsprogramResource utdanningsprogram) {
        return medlemskapService.getMedlemskapResource(Links.get(utdanningsprogram, "medlemskap"));
    }

}

