
package no.fint.graphql.model.model.arbeidslokasjon;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.arbeidsforhold.ArbeidsforholdService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.organisasjon.ArbeidslokasjonResource;
import no.novari.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelArbeidslokasjonResolver")
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

