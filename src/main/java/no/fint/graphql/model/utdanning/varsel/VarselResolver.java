
package no.fint.graphql.model.utdanning.varsel;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.varseltype.VarseltypeService;
import no.fint.graphql.model.utdanning.faggruppemedlemskap.FaggruppemedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.VarselResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.kodeverk.VarseltypeResource;
import no.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningVarselResolver")
public class VarselResolver implements GraphQLResolver<VarselResource> {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private VarseltypeService varseltypeService;

    @Autowired
    private FaggruppemedlemskapService faggruppemedlemskapService;


    public CompletionStage<SkoleressursResource> getUtsteder(VarselResource varsel, DataFetchingEnvironment dfe) {
        return Flux.fromStream(varsel.getUtsteder()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<UndervisningsforholdResource> getKarakteransvarlig(VarselResource varsel, DataFetchingEnvironment dfe) {
        return Flux.fromStream(varsel.getKarakteransvarlig()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<VarseltypeResource> getType(VarselResource varsel, DataFetchingEnvironment dfe) {
        return Flux.fromStream(varsel.getType()
                .stream()
                .map(Link::getHref)
                .map(l -> varseltypeService.getVarseltypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FaggruppemedlemskapResource> getFaggruppemedlemskap(VarselResource varsel, DataFetchingEnvironment dfe) {
        return Flux.fromStream(varsel.getFaggruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> faggruppemedlemskapService.getFaggruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

