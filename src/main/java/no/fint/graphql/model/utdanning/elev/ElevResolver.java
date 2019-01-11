// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.elev;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.felles.person.PersonService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;


import no.fint.model.resource.utdanning.elev.ElevResource;


import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningElevResolver")
public class ElevResolver implements GraphQLResolver<ElevResource> {


    @Autowired
    private PersonService personService;

    @Autowired
    private ElevforholdService elevforholdService;


    public PersonResource getPerson(ElevResource elev, DataFetchingEnvironment dfe) {
        return personService.getPersonResource(
            Links.get(elev.getPerson()),
            dfe);
    }

    public ElevforholdResource getElevforhold(ElevResource elev, DataFetchingEnvironment dfe) {
        return elevforholdService.getElevforholdResource(
            Links.get(elev.getElevforhold()),
            dfe);
    }

}

