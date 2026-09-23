
package no.fint.graphql.model.model.undervisningsgruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelUndervisningsgruppemedlemskapResolver")
public class UndervisningsgruppemedlemskapResolver {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;


    @SchemaMapping(typeName = "Undervisningsgruppemedlemskap", field = "elevforhold")
    public CompletionStage<ElevforholdResource> getElevforhold(UndervisningsgruppemedlemskapResource undervisningsgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppemedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Undervisningsgruppemedlemskap", field = "undervisningsgruppe")
    public CompletionStage<UndervisningsgruppeResource> getUndervisningsgruppe(UndervisningsgruppemedlemskapResource undervisningsgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppemedlemskap.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

