package no.fint.graphql.model.model.person;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.FintPropertyUtils;
import no.fint.graphql.MissingAuthorizationException;
import no.fint.graphql.UnauthorizedResourceAccessException;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.novari.fint.model.resource.felles.PersonResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Comparator;
import java.util.List;
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
        return Mono.just(Stream
                        .of(endpoints.getAdministrasjonPersonal(), endpoints.getUtdanningElev())
                        .map(e -> e + "/person/"
                                + id
                                + "/"
                                + value)
                        .toList())
                .flatMapMany(reactor.core.publisher.Flux::fromIterable)
                .concatMap(url -> fetchPerson(url, dfe))
                .collectList()
                .flatMap(this::resolvePersonResults);
    }

    public Mono<PersonResource> getPersonResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, PersonResource.class, dfe);
    }

    private Mono<PersonLookupResult> fetchPerson(String url, DataFetchingEnvironment dfe) {
        return getPersonResource(url, dfe)
                .map(PersonLookupResult::success)
                .onErrorResume(error -> Mono.just(PersonLookupResult.failure(error)));
    }

    private Mono<PersonResource> resolvePersonResults(List<PersonLookupResult> results) {
        List<PersonResource> resources = results.stream()
                .map(PersonLookupResult::resource)
                .filter(Objects::nonNull)
                .toList();

        if (!resources.isEmpty()) {
            return Mono.just(resources.stream()
                    .reduce(this::mergePersonResources)
                    .orElse(resources.getFirst()));
        }

        Throwable failure = selectFailure(results.stream()
                .map(PersonLookupResult::failure)
                .filter(Objects::nonNull)
                .toList());

        return failure == null ? Mono.empty() : Mono.error(failure);
    }

    private PersonResource mergePersonResources(PersonResource intermediate, PersonResource current) {
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
    }

    private Throwable selectFailure(List<Throwable> failures) {
        if (failures.isEmpty() || failures.stream().allMatch(this::isNotFound)) {
            return null;
        }
        return failures.stream()
                .min(Comparator.comparingInt(this::failurePriority))
                .orElse(null);
    }

    private int failurePriority(Throwable failure) {
        if (failure instanceof MissingAuthorizationException
                || failure instanceof UnauthorizedResourceAccessException
                || hasStatus(failure, 401)
                || hasStatus(failure, 403)) {
            return 0;
        }
        if (failure instanceof no.fint.graphql.WebClientRequestException || isServerError(failure)) {
            return 1;
        }
        if (isNotFound(failure)) {
            return 3;
        }
        return 2;
    }

    private boolean isNotFound(Throwable failure) {
        return hasStatus(failure, 404);
    }

    private boolean isServerError(Throwable failure) {
        return failure instanceof WebClientResponseException responseException
                && responseException.getStatusCode().is5xxServerError();
    }

    private boolean hasStatus(Throwable failure, int status) {
        return failure instanceof WebClientResponseException responseException
                && responseException.getStatusCode().value() == status;
    }

    private record PersonLookupResult(PersonResource resource, Throwable failure) {
        private static PersonLookupResult success(PersonResource resource) {
            return new PersonLookupResult(resource, null);
        }

        private static PersonLookupResult failure(Throwable failure) {
            return new PersonLookupResult(null, failure);
        }
    }
}
