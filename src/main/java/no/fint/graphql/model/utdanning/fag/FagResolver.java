// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.fag;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.utdanning.timeplan.FagResource;


import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningFagResolver")
public class FagResolver implements GraphQLResolver<FagResource> {


    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public ProgramomradeResource getProgramomrade(FagResource fag) {
        return programomradeService.getProgramomradeResource(Links.get(fag, "programomrade"));
    }

    public SkoleResource getSkole(FagResource fag) {
        return skoleService.getSkoleResource(Links.get(fag, "skole"));
    }

    public UndervisningsgruppeResource getUndervisningsgruppe(FagResource fag) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(Links.get(fag, "undervisningsgruppe"));
    }

    public EksamensgruppeResource getEksamensgruppe(FagResource fag) {
        return eksamensgruppeService.getEksamensgruppeResource(Links.get(fag, "eksamensgruppe"));
    }

    public MedlemskapResource getMedlemskap(FagResource fag) {
        return medlemskapService.getMedlemskapResource(Links.get(fag, "medlemskap"));
    }

}

