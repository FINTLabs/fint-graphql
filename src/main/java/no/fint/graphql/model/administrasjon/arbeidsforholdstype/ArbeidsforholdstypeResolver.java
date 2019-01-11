// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforholdstype;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.arbeidsforholdstype.ArbeidsforholdstypeService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("administrasjonArbeidsforholdstypeResolver")
public class ArbeidsforholdstypeResolver implements GraphQLResolver<ArbeidsforholdstypeResource> {

    @Autowired
    private ArbeidsforholdstypeService arbeidsforholdstypeService;


    public ArbeidsforholdstypeResource getForelder(ArbeidsforholdstypeResource arbeidsforholdstype, DataFetchingEnvironment dfe) {
        return arbeidsforholdstype.getForelder()
            .stream()
            .map(Link::getHref)
            .map(l -> arbeidsforholdstypeService.getArbeidsforholdstypeResource(l, dfe))
            .findFirst().orElse(null);
    }

}

