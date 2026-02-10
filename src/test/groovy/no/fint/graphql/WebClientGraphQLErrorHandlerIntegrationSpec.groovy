package no.fint.graphql

import com.coxautodev.graphql.tools.GraphQLResolver
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import graphql.execution.DataFetcherResult
import graphql.execution.ExecutionPath
import graphql.language.SourceLocation
import graphql.schema.DataFetchingEnvironment
import no.fint.graphql.model.Endpoints
import no.fint.graphql.model.model.rolle.RolleService
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.felles.kompleksedatatyper.Periode
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.administrasjon.fullmakt.FullmaktResource
import no.novari.fint.model.resource.administrasjon.fullmakt.RolleResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.QueueDispatcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestApplication,
        properties = "spring.main.allow-bean-definition-overriding=true"
)
@AutoConfigureWebTestClient
class WebClientGraphQLErrorHandlerIntegrationSpec extends Specification {

    @Shared
    private static final MockWebServer server = new MockWebServer()

    @Autowired
    private WebTestClient webTestClient

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        startServerIfNeeded()
        registry.add("fint.endpoint.root", { server.url("/").toString() })
    }

    def cleanupSpec() {
        server.shutdown()
    }

    def setup() {
        // Reset queued responses between tests to avoid cross-test contamination.
        server.setDispatcher(new QueueDispatcher())
    }

    def "GraphQL maps status #status to expected error object"() {
        given:
        server.enqueue(new MockResponse().setResponseCode(status).setBody("error"))
        def query = 'query { rolle(navn: "bar") { navn { identifikatorverdi } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data?.rolle == null
        body.errors?.size() == 1
        body.errors[0].path == ["rolle"]
        body.errors[0].message == expectedMessage(status, "bar")
        assertExtensionsMatch(body.errors[0].extensions, expectedExtensions(status, "/administrasjon/fullmakt/rolle/navn/bar"))

        where:
        status << [401, 403, 404]
    }

    def "GraphQL maps status #status to expected error object for retried requests"() {
        given:
        4.times {
            server.enqueue(new MockResponse().setResponseCode(status).setBody("error"))
        }
        def query = 'query { rolle(navn: "bar") { navn { identifikatorverdi } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data?.rolle == null
        body.errors?.size() == 1
        body.errors[0].path == ["rolle"]
        body.errors[0].message == expectedMessage(status, "bar")
        assertExtensionsMatch(body.errors[0].extensions, expectedExtensions(status, "/administrasjon/fullmakt/rolle/navn/bar"))

        where:
        status << [500, 503]
    }

    def "GraphQL returns message and path for status #status"() {
        given:
        server.enqueue(new MockResponse().setResponseCode(status).setBody("error"))
        def query = 'query { rolle(navn: "bar") { navn { identifikatorverdi } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.errors?.size() == 1
        body.errors[0].message == expectedMessage(status, "bar")
        body.errors[0].path == ["rolle"]

        where:
        status << [401, 403, 404]
    }

    def "GraphQL handles rolle fullmakt links with mixed outcomes"() {
        given:
        drainRequests()
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(fullmaktResource("F1")))
        server.enqueue(new MockResponse().setResponseCode(403).setBody("error"))
        server.enqueue(new MockResponse().setResponseCode(404).setBody("error"))
        def query = 'query { rolle(navn: "foo") { fullmakt { systemId { identifikatorverdi } } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        def expectedData = [
                rolle: [
                        fullmakt: [
                                [systemId: [identifikatorverdi: "F1"]],
                                null,
                                null
                        ]
                ]
        ]
        body.data == expectedData
        body.errors?.size() == 2
        def messages = body.errors.collect { it.message }
        def forbiddenPaths = [
                "Access forbidden for /administrasjon/fullmakt/fullmakt/systemid/2",
                "Access forbidden for /administrasjon/fullmakt/fullmakt/systemid/3"
        ]
        def missingPaths = [
                "Resource not found at /administrasjon/fullmakt/fullmakt/systemid/2",
                "Resource not found at /administrasjon/fullmakt/fullmakt/systemid/3"
        ]
        messages.any { it in forbiddenPaths }
        messages.any { it in missingPaths }
        def paths = body.errors.collect { it.path }
        paths.containsAll([
                ["rolle", "fullmakt", 1],
                ["rolle", "fullmakt", 2]
        ])
        def extensions = body.errors.collect { it.extensions }
        def expectedOne = expectedExtensions(403, "/administrasjon/fullmakt/fullmakt/systemid/2")
        def expectedTwo = expectedExtensions(404, "/administrasjon/fullmakt/fullmakt/systemid/3")
        def expectedAltOne = expectedExtensions(403, "/administrasjon/fullmakt/fullmakt/systemid/3")
        def expectedAltTwo = expectedExtensions(404, "/administrasjon/fullmakt/fullmakt/systemid/2")
        assertExtensionsMatch(extensions[0], expectedOne) || assertExtensionsMatch(extensions[0], expectedAltOne)
        assertExtensionsMatch(extensions[1], expectedTwo) || assertExtensionsMatch(extensions[1], expectedAltTwo)

        and:
        def fullmaktPaths = []
        (0..<6).each {
            def request = server.takeRequest(2, TimeUnit.SECONDS)
            if (request?.path?.startsWith("/administrasjon/fullmakt/fullmakt/")) {
                fullmaktPaths << request.path
            }
        }
        fullmaktPaths.containsAll([
                "/administrasjon/fullmakt/fullmakt/systemid/1",
                "/administrasjon/fullmakt/fullmakt/systemid/2",
                "/administrasjon/fullmakt/fullmakt/systemid/3"
        ])
    }

    private static String expectedMessage(int status, String navn) {
        def resourcePath = "/administrasjon/fullmakt/rolle/navn/${navn}"
        if (status == 401) {
            return "Access unauthorized for ${resourcePath}"
        } else if (status == 403) {
            return "Access forbidden for ${resourcePath}"
        } else if (status == 404) {
            return "Resource not found at ${resourcePath}"
        } else {
            return HttpStatus.resolve(status).getReasonPhrase() + " for " + resourcePath
        }
    }

    private static Map<String, Object> expectedExtensions(int status, String resourcePath) {
        def parts = resourcePath.split("/")
        if (parts.size() == 6 && parts[1..5].every { it }) {
            return [
                    code: status,
                    domain: parts[1],
                    package: parts[2],
                    resource: parts[3],
                    idkey: parts[4],
                    idvalue: parts[5]
            ]
        }
        return [code: status]
    }

    private static boolean assertExtensionsMatch(Map actual, Map expected) {
        expected.every { key, value -> actual[key] == value }
    }

    private static String fullmaktResource(String id) {
        return """
{
  "systemId": { "identifikatorverdi": "${id}" },
  "gyldighetsperiode": { "start": "2020-01-01", "slutt": "2020-12-31" }
}
"""
    }

    private String executeQuery(String query) {
        return webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(20))
                .build()
                .post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: query])
                .exchange()
                .expectStatus().isOk()
                .returnResult(String)
                .responseBody
                .blockFirst()
    }

    private static void drainRequests() {
        while (server.takeRequest(0, TimeUnit.SECONDS) != null) {
            // drain existing requests from prior tests
        }
    }

    private static void startServerIfNeeded() {
        if (server.getPort() != -1) {
            return
        }
        try {
            server.start()
        } catch (IOException ex) {
            throw new RuntimeException(ex)
        }
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @ComponentScan(basePackages = "no.fint.graphql")
    static class TestApplication {
        @Bean
        @Primary
        WebClient testWebClient() {
            startServerIfNeeded()
            return WebClient.builder()
                    .baseUrl(server.url("/").toString())
                    .build()
        }

        @Bean
        @Primary
        RolleService testRolleService(WebClientRequest webClientRequest, Endpoints endpoints) {
            return new RolleService() {
                @Override
                Mono<RolleResource> getRolleResourceById(String id, String value, DataFetchingEnvironment dfe) {
                    if (!"foo".equals(value)) {
                        def url = endpoints.getAdministrasjonFullmakt() + "/rolle/" + id + "/" + value
                        return webClientRequest.get(url, RolleResource.class, dfe)
                    }
                    def rolle = new RolleResource()
                    rolle.setBeskrivelse("Test rolle")
                    def navn = new Identifikator()
                    navn.setIdentifikatorverdi("R1")
                    rolle.setNavn(navn)
                    rolle.addFullmakt(new Link("/administrasjon/fullmakt/fullmakt/systemid/1"))
                    rolle.addFullmakt(new Link("/administrasjon/fullmakt/fullmakt/systemid/2"))
                    rolle.addFullmakt(new Link("/administrasjon/fullmakt/fullmakt/systemid/3"))
                    return Mono.just(rolle)
                }
            }
        }

        @Bean(name = "modelRolleResolver")
        @Primary
        GraphQLResolver<RolleResource> testRolleResolver(WebClientRequest webClientRequest) {
            return new TestRolleResolver(webClientRequest)
        }
    }

    static class TestRolleResolver implements GraphQLResolver<RolleResource> {
        private final WebClientRequest webClientRequest

        TestRolleResolver(WebClientRequest webClientRequest) {
            this.webClientRequest = webClientRequest
        }

        CompletionStage<DataFetcherResult<List<FullmaktResource>>> getFullmakt(RolleResource rolle, DataFetchingEnvironment dfe) {
            def links = rolle.getFullmakt().collect { it.href }
            return Flux.fromIterable(links)
                    .index()
                    .flatMap { tuple ->
                        def idx = tuple.t1.intValue()
                        def href = tuple.t2
                        webClientRequest.get(href, String.class, dfe)
                                .map { res -> [idx, buildFullmaktResource(idx), null] }
                                .onErrorResume { ex -> Mono.just([idx, null, ex]) }
                    }
                    .collectList()
                    .map { results ->
                        def data = new ArrayList<FullmaktResource>(Collections.nCopies(links.size(), null))
                        def errors = new ArrayList<GraphQLError>()
                        results.each { r ->
                            def idx = (int) r[0]
                            def res = (FullmaktResource) r[1]
                            def ex = (Throwable) r[2]
                            if (res != null) {
                                data[idx] = res
                            }
                            if (ex != null) {
                                errors.add(new ExceptionWhileDataFetching(
                                        ExecutionPath.fromList(["rolle", "fullmakt", idx]),
                                        ex,
                                        new SourceLocation(1, 1)
                                ))
                            }
                        }
                        return DataFetcherResult.newResult()
                                .data(data)
                                .errors(errors)
                                .build()
                    }
                    .toFuture()
        }

        private static FullmaktResource buildFullmaktResource(int idx) {
            def fullmakt = new FullmaktResource()
            def systemId = new Identifikator()
            systemId.setIdentifikatorverdi(idx == 0 ? "F1" : "F${idx + 1}")
            fullmakt.setSystemId(systemId)
            def periode = new Periode()
            fullmakt.setGyldighetsperiode(periode)
            return fullmakt
        }
    }
}
