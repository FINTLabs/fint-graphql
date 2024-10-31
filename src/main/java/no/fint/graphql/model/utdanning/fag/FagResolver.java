
package no.fint.graphql.model.utdanning.fag;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.elevtilrettelegging.ElevtilretteleggingService;
import no.fint.graphql.model.utdanning.faggruppe.FaggruppeService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.elev.ElevtilretteleggingResource;
import no.fint.model.resource.utdanning.timeplan.FaggruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningFagResolver")
public class FagResolver implements GraphQLResolver<FagResource> {

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private ElevtilretteleggingService elevtilretteleggingService;

    @Autowired
    private FaggruppeService faggruppeService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<List<ProgramomradeResource>> getProgramomrade(FagResource fag, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fag.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<ElevtilretteleggingResource>> getTilrettelegging(FagResource fag, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fag.getTilrettelegging()
                .stream()
                .map(Link::getHref)
                .map(l -> elevtilretteleggingService.getElevtilretteleggingResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<FaggruppeResource>> getFaggruppe(FagResource fag, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fag.getFaggruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> faggruppeService.getFaggruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SkoleResource>> getSkole(FagResource fag, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fag.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsgruppeResource>> getUndervisningsgruppe(FagResource fag, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fag.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppeResource>> getEksamensgruppe(FagResource fag, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fag.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(FagResource fag, DataFetchingEnvironment dfe) {
        return Flux.fromStream(fag.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

