
package no.fint.graphql.model.model.varsel;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.faggruppemedlemskap.FaggruppemedlemskapService;
import no.fint.graphql.model.model.skoleressurs.SkoleressursService;
import no.fint.graphql.model.model.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.model.varseltype.VarseltypeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.novari.fint.model.resource.utdanning.elev.VarselResource;
import no.novari.fint.model.resource.utdanning.kodeverk.VarseltypeResource;
import no.novari.fint.model.resource.utdanning.timeplan.FaggruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelVarselResolver")
public class VarselResolver {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private VarseltypeService varseltypeService;

    @Autowired
    private FaggruppemedlemskapService faggruppemedlemskapService;


    @SchemaMapping(typeName = "Varsel", field = "utsteder")
    public CompletionStage<SkoleressursResource> getUtsteder(VarselResource varsel, DataFetchingEnvironment dfe) {
        return Flux.fromStream(varsel.getUtsteder()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Varsel", field = "karakteransvarlig")
    public CompletionStage<UndervisningsforholdResource> getKarakteransvarlig(VarselResource varsel, DataFetchingEnvironment dfe) {
        return Flux.fromStream(varsel.getKarakteransvarlig()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Varsel", field = "type")
    public CompletionStage<VarseltypeResource> getType(VarselResource varsel, DataFetchingEnvironment dfe) {
        return Flux.fromStream(varsel.getType()
                .stream()
                .map(Link::getHref)
                .map(l -> varseltypeService.getVarseltypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Varsel", field = "faggruppemedlemskap")
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

