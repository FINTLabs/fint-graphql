package no.fint.graphql

import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import mockwebserver3.RecordedRequest
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/** Runs with the application's own beans and defaults, only the threshold is lowered. */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, TestJwtDecoderConfig],
        properties = ['fint.graphql.relation-pagination.threshold=2', 'fint.graphql.blacklist=']
)
@AutoConfigureWebTestClient
class RelationPaginationIntegrationSpec extends Specification {
    private static final String BASE = 'https://beta.felleskomponent.no'
    private static final String SKOLE = '/utdanning/utdanningsprogram/skole/systemid/S'
    private static final String COLLECTION = '/utdanning/elev/elevforhold'
    private static final RecordingDispatcher DISPATCHER = new RecordingDispatcher()
    private static final MockWebServer SERVER = startServer()

    @Autowired
    WebTestClient client

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add('fint.endpoint.root', { SERVER.url('/').toString() })
    }

    def cleanupSpec() {
        SERVER.close()
    }

    private static MockWebServer startServer() {
        def server = new MockWebServer()
        server.dispatcher = DISPATCHER
        server.start()
        server
    }

    def "a GraphQL query reads a large relation from pages larger than the 256 KB codec default"() {
        given:
        def filler = 'x' * 100_000
        def page = { List<String> ids, String next ->
            def entries = ids.collect { id ->
                """{"systemId":{"identifikatorverdi":"$id"},"beskrivelse":"$filler",
                    "gyldighetsperiode":{"start":"2026-08-17T00:00:00Z"},
                    "_links":{"self":[{"href":"$BASE$COLLECTION/systemid/$id"}]}}"""
            }.join(',')
            def links = next ? """"next":[{"href":"$BASE$next"}]""" : ''
            json("""{"_embedded":{"_entries":[$entries]},"_links":{$links}}""")
        }
        def first = ('0'..'4').collect { "ID-$it".toString() }
        def second = ('5'..'9').collect { "ID-$it".toString() }
        DISPATCHER.responses[SKOLE] = json("""{"navn":"Skolen","_links":{"elevforhold":[
                {"href":"$BASE$COLLECTION/systemid/ID-7"},
                {"href":"$BASE$COLLECTION/systemid/ID-1"},
                {"href":"$BASE$COLLECTION/systemid/ID-9"}]}}""")
        DISPATCHER.responses["$COLLECTION?size=500".toString()] = page(first, "$COLLECTION?offset=5&size=500&cursor=c1")
        DISPATCHER.responses["$COLLECTION?offset=5&size=500&cursor=c1".toString()] = page(second, null)

        when:
        def response = client.post().uri('/graphql')
                .header('Authorization', TestJwtTokens.bearerWithRoles('FINT_Client_UtdanningElev', 'FINT_Client_UtdanningUtdanningsprogram'))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: '{ skole(systemId: "S") { elevforhold { systemId { identifikatorverdi } gyldighetsperiode { start } } } }'])
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map)
                .returnResult()
                .responseBody

        then:
        response.errors == null
        response.data.skole.elevforhold*.systemId*.identifikatorverdi == ['ID-7', 'ID-1', 'ID-9']
        response.data.skole.elevforhold*.gyldighetsperiode*.start.every { it != null }
        DISPATCHER.targets == [SKOLE, "$COLLECTION?size=500", "$COLLECTION?offset=5&size=500&cursor=c1"]
    }

    private static MockResponse json(String body) {
        new MockResponse.Builder().code(200).setHeader(HttpHeaders.CONTENT_TYPE, 'application/json').body(body).build()
    }
}

class RecordingDispatcher extends Dispatcher {
    final List<String> targets = new CopyOnWriteArrayList<>()
    final Map<String, MockResponse> responses = new ConcurrentHashMap<>()

    @Override
    MockResponse dispatch(@NotNull RecordedRequest request) {
        targets << request.target
        responses.getOrDefault(request.target, new MockResponse.Builder().code(404).build())
    }
}
