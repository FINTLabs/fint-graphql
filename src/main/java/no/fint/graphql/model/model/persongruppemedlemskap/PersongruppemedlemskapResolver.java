
package no.fint.graphql.model.model.persongruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.persongruppe.PersongruppeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.elev.PersongruppeResource;
import no.novari.fint.model.resource.utdanning.elev.PersongruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelPersongruppemedlemskapResolver")
public class PersongruppemedlemskapResolver implements GraphQLResolver<PersongruppemedlemskapResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private PersongruppeService persongruppeService;


    public CompletionStage<ElevforholdResource> getElevforhold(PersongruppemedlemskapResource persongruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppemedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<PersongruppeResource> getPersongruppe(PersongruppemedlemskapResource persongruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(persongruppemedlemskap.getPersongruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> persongruppeService.getPersongruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

