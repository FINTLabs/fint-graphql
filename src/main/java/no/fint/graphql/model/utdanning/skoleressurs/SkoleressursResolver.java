// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.skole.SkoleService;


import no.fint.model.resource.utdanning.elev.SkoleressursResource;


import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningSkoleressursResolver")
public class SkoleressursResolver implements GraphQLResolver<SkoleressursResource> {


    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private SkoleService skoleService;


    public PersonalressursResource getPersonalressurs(SkoleressursResource skoleressurs) {
        return personalressursService.getPersonalressursResource(Links.get(skoleressurs, "personalressurs"));
    }

    public UndervisningsforholdResource getUndervisningsforhold(SkoleressursResource skoleressurs) {
        return undervisningsforholdService.getUndervisningsforholdResource(Links.get(skoleressurs, "undervisningsforhold"));
    }

    public SkoleResource getSkole(SkoleressursResource skoleressurs) {
        return skoleService.getSkoleResource(Links.get(skoleressurs, "skole"));
    }

}

