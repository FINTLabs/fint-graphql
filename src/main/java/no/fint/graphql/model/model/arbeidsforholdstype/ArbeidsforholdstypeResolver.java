
package no.fint.graphql.model.model.arbeidsforholdstype;

import graphql.schema.DataFetchingEnvironment;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelArbeidsforholdstypeResolver")
public class ArbeidsforholdstypeResolver {

    @Autowired
    private ArbeidsforholdstypeService arbeidsforholdstypeService;


    @SchemaMapping(typeName = "Arbeidsforholdstype", field = "forelder")
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

