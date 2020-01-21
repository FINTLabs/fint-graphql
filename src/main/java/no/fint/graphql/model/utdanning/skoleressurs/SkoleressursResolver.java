
package no.fint.graphql.model.utdanning.skoleressurs;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.skole.SkoleService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningSkoleressursResolver")
public class SkoleressursResolver implements GraphQLResolver<SkoleressursResource> {

    @Autowired
    private PersonalressursService personalressursService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private SkoleService skoleService;


    public CompletionStage<PersonalressursResource> getPersonalressurs(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skoleressurs.getPersonalressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> personalressursService.getPersonalressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skoleressurs.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SkoleResource>> getSkole(SkoleressursResource skoleressurs, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skoleressurs.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

