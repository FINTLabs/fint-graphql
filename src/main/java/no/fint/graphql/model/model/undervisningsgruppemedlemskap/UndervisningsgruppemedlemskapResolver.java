
package no.fint.graphql.model.model.undervisningsgruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.undervisningsgruppe.UndervisningsgruppeService;


import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppemedlemskapResource;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("modelUndervisningsgruppemedlemskapResolver")
public class UndervisningsgruppemedlemskapResolver implements GraphQLResolver<UndervisningsgruppemedlemskapResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;


    public CompletionStage<ElevforholdResource> getElevforhold(UndervisningsgruppemedlemskapResource undervisningsgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(undervisningsgruppemedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

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

