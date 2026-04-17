
package no.fint.graphql.model.model.arstrinn;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.programomrade.ProgramomradeService;
import no.fint.graphql.model.model.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.model.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelArstrinnResolver")
public class ArstrinnResolver implements GraphQLResolver<ArstrinnResource> {

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private BasisgruppeService basisgruppeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<List<ProgramomradeResource>> getProgramomrade(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arstrinn.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<BasisgruppeResource>> getBasisgruppe(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arstrinn.getBasisgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(ArstrinnResource arstrinn, DataFetchingEnvironment dfe) {
        return Flux.fromStream(arstrinn.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

