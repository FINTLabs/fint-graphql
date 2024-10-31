
package no.fint.graphql.model.administrasjon.arbeidslokasjon;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.organisasjon.ArbeidslokasjonResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("administrasjonArbeidslokasjonResolver")
public class ArbeidslokasjonResolver implements GraphQLResolver<ArbeidslokasjonResource> {

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;


    public CompletionStage<List<ArbeidsforholdResource>> getArbeidsforhold(ArbeidslokasjonResource arbeidslokasjon, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arbeidslokasjon.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

