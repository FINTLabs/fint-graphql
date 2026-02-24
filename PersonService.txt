package no.fint.graphql.model.model.person;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.FintPropertyUtils;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("modelPersonService")
public class PersonService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public Mono<PersonResource> getPersonResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return Flux.fromStream(Stream
                        .of(endpoints.getAdministrasjonPersonal(), endpoints.getUtdanningElev())
                        .map(e -> e + "/person/"
                                + id
                                + "/"
                                + value)
                        .map(r -> getPersonResource(r, dfe)))
                .flatMap(Mono::flux)
                .filter(Objects::nonNull)
                .reduce((intermediate, current) -> {
                    FintPropertyUtils.copyProperties(
                            current, intermediate,
                            pd -> !pd.getName().contentEquals("links"),
                            (a, b) -> b == null ? a : b
                    );
                    current.getLinks().forEach((link, target) -> {
                        if (intermediate.getLinks().containsKey(link)) {
                            intermediate.getLinks().put(link,
                                    Stream.concat(target.stream(),
                                                    intermediate.getLinks().get(link).stream())
                                            .distinct()
                                            .collect(Collectors.toList()));
                        } else {
                            intermediate.getLinks().put(link, target);
                        }
                    });
                    return intermediate;
                });
    }

    public Mono<PersonResource> getPersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonResource.class, dfe);
    }
}
