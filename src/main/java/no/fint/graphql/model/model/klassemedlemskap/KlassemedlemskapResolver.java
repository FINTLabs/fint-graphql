
package no.fint.graphql.model.model.klassemedlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.klasse.KlasseService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.KlassemedlemskapResource;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.elev.KlasseResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelKlassemedlemskapResolver")
public class KlassemedlemskapResolver implements GraphQLResolver<KlassemedlemskapResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private KlasseService klasseService;


    public CompletionStage<ElevforholdResource> getElevforhold(KlassemedlemskapResource klassemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(klassemedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

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

