
package no.fint.graphql.model.utdanning.elev;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.person.PersonService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningElevResolver")
public class ElevResolver implements GraphQLResolver<ElevResource> {

    @Autowired
    private PersonService personService;

    @Autowired
    private ElevforholdService elevforholdService;


    public PersonResource getPerson(ElevResource elev, DataFetchingEnvironment dfe) {
        return elev.getPerson()
                .stream()
                .map(Link::getHref)
                .map(l -> personService.getPersonResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<ElevforholdResource> getElevforhold(ElevResource elev, DataFetchingEnvironment dfe) {
        return elev.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

