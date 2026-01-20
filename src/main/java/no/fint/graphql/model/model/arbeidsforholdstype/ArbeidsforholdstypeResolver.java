
package no.fint.graphql.model.model.arbeidsforholdstype;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.arbeidsforholdstype.ArbeidsforholdstypeService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.novari.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelArbeidsforholdstypeResolver")
public class ArbeidsforholdstypeResolver implements GraphQLResolver<ArbeidsforholdstypeResource> {

    @Autowired
    private ArbeidsforholdstypeService arbeidsforholdstypeService;


    public CompletionStage<ArbeidsforholdstypeResource> getForelder(ArbeidsforholdstypeResource arbeidsforholdstype, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidsforholdstype.getForelder()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdstypeService.getArbeidsforholdstypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

