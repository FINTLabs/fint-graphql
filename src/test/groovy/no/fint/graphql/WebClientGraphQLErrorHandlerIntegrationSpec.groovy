package no.fint.graphql

import com.coxautodev.graphql.tools.GraphQLResolver
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import graphql.execution.DataFetcherResult
import graphql.execution.ResultPath
import graphql.language.SourceLocation
import graphql.schema.DataFetchingEnvironment
import no.fint.graphql.model.Endpoints
import no.fint.graphql.model.model.rolle.RolleService
import no.fint.model.felles.kompleksedatatyper.Identifikator
import no.fint.model.felles.kompleksedatatyper.Periode
import no.fint.model.felles.kompleksedatatyper.Personnavn
import no.fint.model.resource.Link
import no.fint.model.resource.administrasjon.personal.PersonalressursResource
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource
import no.fint.model.resource.administrasjon.fullmakt.RolleResource
import no.fint.model.resource.felles.PersonResource
import no.fint.model.resource.utdanning.elev.ElevResource
import no.fint.model.resource.utdanning.vurdering.HalvarsfagvurderingResource
import no.fint.model.resource.utdanning.vurdering.KarakterverdiResource
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.QueueDispatcher
import okhttp3.mockwebserver.RecordedRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
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
        classes = [TestApplication, TestJwtDecoderConfig],
        properties = "spring.main.allow-bean-definition-overriding=true"
)
@ContextConfiguration
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

    def "DataLoader dedupes identical resource requests within a query"() {
        given:
        drainRequests()
        // Always return 200 so the only thing we're validating is request count,
        // not any particular error mapping.
        server.setDispatcher(new Dispatcher() {
            @Override
            MockResponse dispatch(RecordedRequest request) {
                return new MockResponse().setResponseCode(200).setBody("ok")
            }
        })
        // The "dedupe" path returns 3 links, two of which are identical.
        // If DataLoader dedupe works, only two backend requests should be made.
        def query = 'query { rolle(navn: "dedupe") { fullmakt { systemId { identifikatorverdi } } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.errors == null || body.errors.isEmpty()
        body.data?.rolle?.fullmakt?.size() == 3

        // We expect two requests (systemid/1 and systemid/2). A third request would mean
        // duplicate links were not deduped within the same GraphQL execution.
        def firstRequest = server.takeRequest(1, TimeUnit.SECONDS)
        firstRequest != null
        def secondRequest = server.takeRequest(1, TimeUnit.SECONDS)
        secondRequest != null
        def thirdRequest = server.takeRequest(200, TimeUnit.MILLISECONDS)
        thirdRequest == null
    }

    def "GraphQL completes when DataLoader loads are enqueued late"() {
        given:
        drainRequests()
        server.enqueue(new MockResponse().setResponseCode(200).setBody("ok"))
        def query = 'query { rolle(navn: "late") { fullmakt { systemId { identifikatorverdi } } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.errors == null || body.errors.isEmpty()
        body.data?.rolle?.fullmakt?.size() == 1
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

    def "GraphQL blocks downstream request when JWT role prefix does not match resource path"() {
        given:
        drainRequests()
        def query = 'query { rolle(navn: "bar") { navn { identifikatorverdi } } }'

        when:
        def responseBody = executeQuery(
                query,
                TestJwtTokens.bearerWithRoles("FINT_Client_UtdanningElev"),
                HttpStatus.OK
        )

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data?.rolle == null
        body.errors?.size() == 1
        body.errors[0].message == "Forbidden"
        body.errors[0].path == ["rolle"]
        body.errors[0].extensions?.code == 403
        server.takeRequest(200, TimeUnit.MILLISECONDS) == null
    }

    def "GraphQL resolves absolute encoded karakter link without double encoding path and with configured Host header"() {
        given:
        drainRequests()
        def halvarsfagvurderingId = "H1"
        def encodedKarakterPath = "/utdanning/vurdering/karakterverdi/systemid/V%3A%3A4"
        def absoluteKarakterLink = server.url(encodedKarakterPath).toString()
        server.setDispatcher(new Dispatcher() {
            @Override
            MockResponse dispatch(RecordedRequest request) {
                if (request.path == "/utdanning/vurdering/halvarsfagvurdering/systemid/${halvarsfagvurderingId}") {
                    return jsonResponse(halvarsfagvurderingResource("HV-1", absoluteKarakterLink))
                }
                if (request.path == encodedKarakterPath) {
                    return jsonResponse(karakterverdiResource("V::4"))
                }
                return new MockResponse().setResponseCode(500).setBody("unexpected ${request.path}")
            }
        })
        def query = """
query {
  halvarsfagvurdering(systemId: "${halvarsfagvurderingId}") {
    systemId { identifikatorverdi }
    karakter { systemId { identifikatorverdi } }
  }
}
"""

        when:
        def responseBody = executeQuery(
                query,
                TestJwtTokens.bearerWithRoles("FINT_Client_UtdanningVurdering")
        )

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.errors == null || body.errors.isEmpty()
        body.data?.halvarsfagvurdering?.systemId?.identifikatorverdi == "HV-1"
        body.data?.halvarsfagvurdering?.karakter?.systemId?.identifikatorverdi == "V::4"

        and:
        def firstRequest = server.takeRequest(1, TimeUnit.SECONDS)
        def secondRequest = server.takeRequest(1, TimeUnit.SECONDS)
        firstRequest.path == "/utdanning/vurdering/halvarsfagvurdering/systemid/${halvarsfagvurderingId}"
        secondRequest.path == encodedKarakterPath
        secondRequest.getHeader('Host') == 'beta.felleskomponent.no'
        secondRequest.path != "/utdanning/vurdering/karakterverdi/systemid/V%253A%253A4"
        server.takeRequest(200, TimeUnit.MILLISECONDS) == null
    }

    def "GraphQL handles rolle fullmakt links with mixed outcomes"() {
        given:
        drainRequests()
        server.setDispatcher(new Dispatcher() {
            @Override
            MockResponse dispatch(RecordedRequest request) {
                if (request.path?.contains("/administrasjon/fullmakt/fullmakt/systemid/1")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody(fullmaktResource("F1"))
                }
                if (request.path?.contains("/administrasjon/fullmakt/fullmakt/systemid/2")) {
                    return new MockResponse().setResponseCode(403).setBody("error")
                }
                if (request.path?.contains("/administrasjon/fullmakt/fullmakt/systemid/3")) {
                    return new MockResponse().setResponseCode(404).setBody("error")
                }
                return new MockResponse().setResponseCode(500).setBody("unexpected")
            }
        })
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
        extensions.any { assertExtensionsMatch(it, expectedOne) || assertExtensionsMatch(it, expectedAltOne) }
        extensions.any { assertExtensionsMatch(it, expectedTwo) || assertExtensionsMatch(it, expectedAltTwo) }

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

    def "Person query returns administrasjon person when utdanning person is not found"() {
        given:
        drainRequests()
        def fnr = "12345678910"
        server.setDispatcher(new Dispatcher() {
            @Override
            MockResponse dispatch(RecordedRequest request) {
                if (request.path == "/administrasjon/personal/person/fodselsnummer/${fnr}") {
                    return jsonResponse(personResource(
                            fnr,
                            "Ada",
                            "Lovelace",
                            null,
                            "admin-image",
                            null,
                            null
                    ))
                }
                if (request.path == "/utdanning/elev/person/fodselsnummer/${fnr}") {
                    return new MockResponse().setResponseCode(404).setBody("not found")
                }
                return new MockResponse().setResponseCode(500).setBody("unexpected")
            }
        })

        when:
        def responseBody = executePersonQuery(fnr)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.errors == null || body.errors.isEmpty()
        body.data?.person?.fodselsnummer?.identifikatorverdi == fnr
        body.data?.person?.navn == [fornavn: "Ada", etternavn: "Lovelace", mellomnavn: null]
        body.data?.person?.bilde == "admin-image"
    }

    def "Person query returns utdanning person when administrasjon person is not found"() {
        given:
        drainRequests()
        def fnr = "12345678910"
        server.setDispatcher(new Dispatcher() {
            @Override
            MockResponse dispatch(RecordedRequest request) {
                if (request.path == "/administrasjon/personal/person/fodselsnummer/${fnr}") {
                    return new MockResponse().setResponseCode(404).setBody("not found")
                }
                if (request.path == "/utdanning/elev/person/fodselsnummer/${fnr}") {
                    return jsonResponse(personResource(
                            fnr,
                            "Grace",
                            "Hopper",
                            null,
                            "elev-image",
                            null,
                            null
                    ))
                }
                return new MockResponse().setResponseCode(500).setBody("unexpected")
            }
        })

        when:
        def responseBody = executePersonQuery(fnr)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.errors == null || body.errors.isEmpty()
        body.data?.person?.fodselsnummer?.identifikatorverdi == fnr
        body.data?.person?.navn == [fornavn: "Grace", etternavn: "Hopper", mellomnavn: null]
        body.data?.person?.bilde == "elev-image"
    }

    def "Person query merges administrasjon and utdanning person results when both succeed"() {
        given:
        drainRequests()
        def fnr = "12345678910"
        server.setDispatcher(new Dispatcher() {
            @Override
            MockResponse dispatch(RecordedRequest request) {
                if (request.path == "/administrasjon/personal/person/fodselsnummer/${fnr}") {
                    return jsonResponse(personResource(
                            fnr,
                            "Ada",
                            "Byron",
                            null,
                            "admin-image",
                            "/administrasjon/personal/personalressurs/systemid/P-1",
                            null
                    ))
                }
                if (request.path == "/utdanning/elev/person/fodselsnummer/${fnr}") {
                    return jsonResponse(personResource(
                            fnr,
                            "Grace",
                            "Hopper",
                            null,
                            null,
                            null,
                            "/utdanning/elev/elev/systemid/E-1"
                    ))
                }
                if (request.path == "/administrasjon/personal/personalressurs/systemid/P-1") {
                    return jsonResponse(personalressursResource("P-1"))
                }
                if (request.path == "/utdanning/elev/elev/systemid/E-1") {
                    return jsonResponse(elevResource("E-1"))
                }
                return new MockResponse().setResponseCode(500).setBody("unexpected")
            }
        })

        when:
        def responseBody = executePersonQuery(fnr)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.errors == null || body.errors.isEmpty()
        body.data?.person?.fodselsnummer?.identifikatorverdi == fnr
        body.data?.person?.bilde == "admin-image"
        body.data?.person?.navn != null
        body.data?.person?.personalressurs?.systemId?.identifikatorverdi == "P-1"
        body.data?.person?.elev?.systemId?.identifikatorverdi == "E-1"
    }

    def "Person query returns null without errors when both person endpoints return 404"() {
        given:
        drainRequests()
        def fnr = "12345678910"
        server.setDispatcher(new Dispatcher() {
            @Override
            MockResponse dispatch(RecordedRequest request) {
                if (request.path == "/administrasjon/personal/person/fodselsnummer/${fnr}") {
                    return new MockResponse().setResponseCode(404).setBody("not found")
                }
                if (request.path == "/utdanning/elev/person/fodselsnummer/${fnr}") {
                    return new MockResponse().setResponseCode(404).setBody("not found")
                }
                return new MockResponse().setResponseCode(500).setBody("unexpected")
            }
        })

        when:
        def responseBody = executePersonQuery(fnr)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data?.person == null
        body.errors == null || body.errors.isEmpty()
    }

    def "Person query surfaces the most relevant downstream error when both person endpoints fail"() {
        given:
        drainRequests()
        def fnr = "12345678910"
        server.setDispatcher(new Dispatcher() {
            @Override
            MockResponse dispatch(RecordedRequest request) {
                if (request.path == "/administrasjon/personal/person/fodselsnummer/${fnr}") {
                    return new MockResponse().setResponseCode(404).setBody("not found")
                }
                if (request.path == "/utdanning/elev/person/fodselsnummer/${fnr}") {
                    return new MockResponse().setResponseCode(503).setBody("service unavailable")
                }
                return new MockResponse().setResponseCode(500).setBody("unexpected")
            }
        })

        when:
        def responseBody = executePersonQuery(fnr)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data?.person == null
        body.errors?.size() == 1
        body.errors[0].path == ["person"]
        body.errors[0].message == "Service Unavailable for /utdanning/elev/person/fodselsnummer/${fnr}"
        assertExtensionsMatch(body.errors[0].extensions, expectedExtensions(503, "/utdanning/elev/person/fodselsnummer/${fnr}"))
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

    private static MockResponse jsonResponse(String body) {
        return new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(body)
    }

    private static String personResource(
            String fodselsnummer,
            String fornavn,
            String etternavn,
            String mellomnavn,
            String bilde,
            String personalressursHref,
            String elevHref
    ) {
        def person = new PersonResource()
        person.setFodselsnummer(identifikator(fodselsnummer))
        def navn = new Personnavn()
        navn.setFornavn(fornavn)
        navn.setEtternavn(etternavn)
        navn.setMellomnavn(mellomnavn)
        person.setNavn(navn)
        person.setBilde(bilde)
        if (personalressursHref != null) {
            person.addPersonalressurs(new Link(personalressursHref))
        }
        if (elevHref != null) {
            person.addElev(new Link(elevHref))
        }
        return new ObjectMapper().writeValueAsString(person)
    }

    private static String halvarsfagvurderingResource(String systemIdValue, String karakterHref) {
        def halvarsfagvurdering = new HalvarsfagvurderingResource()
        halvarsfagvurdering.setSystemId(identifikator(systemIdValue))
        halvarsfagvurdering.addKarakter(new Link(karakterHref))
        return new ObjectMapper().writeValueAsString(halvarsfagvurdering)
    }

    private static String karakterverdiResource(String systemIdValue) {
        def karakterverdi = new KarakterverdiResource()
        karakterverdi.setSystemId(identifikator(systemIdValue))
        return new ObjectMapper().writeValueAsString(karakterverdi)
    }

    private static String personalressursResource(String systemIdValue) {
        def personalressurs = new PersonalressursResource()
        personalressurs.setSystemId(identifikator(systemIdValue))
        return new ObjectMapper().writeValueAsString(personalressurs)
    }

    private static String elevResource(String systemIdValue) {
        def elev = new ElevResource()
        elev.setSystemId(identifikator(systemIdValue))
        return new ObjectMapper().writeValueAsString(elev)
    }

    private static Identifikator identifikator(String value) {
        def identifikator = new Identifikator()
        identifikator.setIdentifikatorverdi(value)
        return identifikator
    }

    private String executeQuery(
            String query,
            String authorization = TestJwtTokens.bearerWithRoles("FINT_Client_AdministrasjonFullmakt"),
            HttpStatusCode expectedStatus = HttpStatus.OK
    ) {
        return webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(20))
                .build()
                .post()
                .uri("/graphql")
                .header("Authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: query])
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .returnResult(String)
                .responseBody
                .blockFirst()
    }

    private String executePersonQuery(
            String fodselsnummer,
            String authorization = TestJwtTokens.bearerWithRoles("FINT_Client_AdministrasjonPersonal", "FINT_Client_UtdanningElev"),
            HttpStatusCode expectedStatus = HttpStatus.OK
    ) {
        def query = """
query {
  person(fodselsnummer: "${fodselsnummer}") {
    bilde
    fodselsnummer { identifikatorverdi }
    navn { fornavn etternavn mellomnavn }
    personalressurs { systemId { identifikatorverdi } }
    elev { systemId { identifikatorverdi } }
  }
}
"""
        return executeQuery(query, authorization, expectedStatus)
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

    @TestConfiguration
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
                    if ("dedupe".equals(value)) {
                        def rolle = new RolleResource()
                        rolle.setBeskrivelse("Dedupe rolle")
                        def navn = new Identifikator()
                        navn.setIdentifikatorverdi("D1")
                        rolle.setNavn(navn)
                        rolle.addFullmakt(new Link("/administrasjon/fullmakt/fullmakt/systemid/1"))
                        rolle.addFullmakt(new Link("/administrasjon/fullmakt/fullmakt/systemid/1"))
                        rolle.addFullmakt(new Link("/administrasjon/fullmakt/fullmakt/systemid/2"))
                        return Mono.just(rolle)
                    }
                    if ("late".equals(value)) {
                        def rolle = new RolleResource()
                        rolle.setBeskrivelse("late")
                        def navn = new Identifikator()
                        navn.setIdentifikatorverdi("late")
                        rolle.setNavn(navn)
                        rolle.addFullmakt(new Link("/administrasjon/fullmakt/fullmakt/systemid/1"))
                        return Mono.just(rolle)
                    }
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
            def delayed = "late" == rolle?.getBeskrivelse()
            return Flux.fromIterable(links)
                    .index()
                    .flatMap { tuple ->
                        def idx = tuple.t1.intValue()
                        def href = tuple.t2
                        def request = webClientRequest.get(href, String.class, dfe)
                        if (delayed) {
                            request = Mono.delay(Duration.ofMillis(50)).then(request)
                        }
                        request
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
                                        ResultPath.fromList(["rolle", "fullmakt", idx]),
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
