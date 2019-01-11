// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.fullmakt;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.administrasjon.rolle.RolleService;


import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;


import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.fullmakt.RolleResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonFullmaktResolver")
public class FullmaktResolver implements GraphQLResolver<FullmaktResource> {


    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private RolleService rolleService;


    public PersonalressursResource getStedfortreder(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return personalressursService.getPersonalressursResource(
            Links.get(fullmakt.getStedfortreder()),
            dfe);
    }

    public PersonalressursResource getFullmektig(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return personalressursService.getPersonalressursResource(
            Links.get(fullmakt.getFullmektig()),
            dfe);
    }

    public RolleResource getRolle(FullmaktResource fullmakt, DataFetchingEnvironment dfe) {
        return rolleService.getRolleResource(
            Links.get(fullmakt.getRolle()),
            dfe);
    }

}

