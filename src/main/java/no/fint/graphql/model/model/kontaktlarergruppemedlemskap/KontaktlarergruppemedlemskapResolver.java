
package no.fint.graphql.model.model.kontaktlarergruppemedlemskap;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.elevforhold.ElevforholdService;
import no.fint.graphql.model.model.kontaktlarergruppe.KontaktlarergruppeService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.novari.fint.model.resource.utdanning.elev.KontaktlarergruppemedlemskapResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelKontaktlarergruppemedlemskapResolver")
public class KontaktlarergruppemedlemskapResolver {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;


    @SchemaMapping(typeName = "Kontaktlarergruppemedlemskap", field = "elevforhold")
    public CompletionStage<ElevforholdResource> getElevforhold(KontaktlarergruppemedlemskapResource kontaktlarergruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppemedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Kontaktlarergruppemedlemskap", field = "kontaktlarergruppe")
    public CompletionStage<KontaktlarergruppeResource> getKontaktlarergruppe(KontaktlarergruppemedlemskapResource kontaktlarergruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(kontaktlarergruppemedlemskap.getKontaktlarergruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppeService.getKontaktlarergruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

