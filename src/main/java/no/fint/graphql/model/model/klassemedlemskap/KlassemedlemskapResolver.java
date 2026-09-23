
package no.fint.graphql.model.model.klassemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.klasse.KlasseService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.elev.KlasseResource;
import no.novari.fint.model.resource.utdanning.elev.KlassemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKlassemedlemskapResolver")
public class KlassemedlemskapResolver {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private KlasseService klasseService;


    @SchemaMapping(typeName = "Klassemedlemskap", field = "elevforhold")
    public CompletionStage<ElevforholdResource> getElevforhold(KlassemedlemskapResource klassemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klassemedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Klassemedlemskap", field = "klasse")
    public CompletionStage<KlasseResource> getKlasse(KlassemedlemskapResource klassemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klassemedlemskap.getKlasse()
                .stream()
                .map(Link::getHref)
                .map(l -> klasseService.getKlasseResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

