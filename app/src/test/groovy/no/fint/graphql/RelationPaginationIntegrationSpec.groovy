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

/** Runs with the application's own beans and configuration, only the thresholds are adjusted. */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, TestJwtDecoderConfig],
        properties = [
                'fint.graphql.relation-pagination.threshold=2',
                'fint.graphql.relation-pagination.relations.klasse.klassemedlemskap=3',
                'fint.graphql.blacklist='
        ]
)
@AutoConfigureWebTestClient
class RelationPaginationIntegrationSpec extends Specification {
    private static final String BASE = 'https://beta.felleskomponent.no'
    private static final String SKOLE = '/utdanning/utdanningsprogram/skole/systemid/S'
    private static final String COLLECTION = '/utdanning/elev/elevforhold'
    private static final String ELEV = '/utdanning/elev/elev'
    private static final String ELEV_FIELD = 'elev { systemId { identifikatorverdi } }'
    private static final RecordingDispatcher DISPATCHER = new RecordingDispatcher()
    private static final MockWebServer SERVER = startServer()

    @Autowired
    WebTestClient client

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add('fint.endpoint.root', { SERVER.url('/').toString() })
    }

    def setup() {
        DISPATCHER.targets.clear()
        DISPATCHER.responses.clear()
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

    def "a configured single-valued relation shares one walk across all its parents"() {
        given: "a skole whose elevforhold each point at their own elev"
        stubSkole(['EF-1', 'EF-2', 'EF-3'])
        DISPATCHER.responses["$COLLECTION?size=500".toString()] = page([
                elevforhold('EF-1', 'E-1'), elevforhold('EF-2', 'E-2'), elevforhold('EF-3', 'E-3')], null)
        DISPATCHER.responses["$ELEV?size=500".toString()] = page(
                [elev('E-1'), elev('E-2')], "$ELEV?offset=2&size=500&cursor=c1")
        DISPATCHER.responses["$ELEV?offset=2&size=500&cursor=c1".toString()] = page([elev('E-3')], null)

        when:
        def response = query("{ skole(systemId: \"S\") { elevforhold { $ELEV_FIELD } } }")

        then:
        response.errors == null
        response.data.skole.elevforhold*.elev*.systemId*.identifikatorverdi == ['E-1', 'E-2', 'E-3']
        DISPATCHER.targets == [SKOLE, "$COLLECTION?size=500", "$ELEV?size=500", "$ELEV?offset=2&size=500&cursor=c1"]
    }

    def "a single parent of a configured relation still requests its one resource directly"() {
        given:
        DISPATCHER.responses["$COLLECTION/systemid/EF-1".toString()] = json(elevforhold('EF-1', 'E-1'))
        DISPATCHER.responses["$ELEV/systemid/E-1".toString()] = json(elev('E-1'))

        when:
        def response = query("{ elevforhold(systemId: \"EF-1\") { $ELEV_FIELD } }")

        then:
        response.errors == null
        response.data.elevforhold.elev.systemId.identifikatorverdi == 'E-1'
        DISPATCHER.targets == ["$COLLECTION/systemid/EF-1", "$ELEV/systemid/E-1"]
    }

    def "a resource missing from the pages of a configured relation is requested individually and fails as before"() {
        given:
        stubSkole(['EF-1', 'EF-2', 'EF-3'])
        DISPATCHER.responses["$COLLECTION?size=500".toString()] = page([
                elevforhold('EF-1', 'E-1'), elevforhold('EF-2', 'GONE'), elevforhold('EF-3', 'E-3')], null)
        DISPATCHER.responses["$ELEV?size=500".toString()] = page([elev('E-1')], null)
        DISPATCHER.responses["$ELEV/systemid/E-3".toString()] = json(elev('E-3'))

        when:
        def response = query("{ skole(systemId: \"S\") { elevforhold { $ELEV_FIELD } } }")

        then:
        response.errors*.path == [['skole', 'elevforhold', 1, 'elev']]
        response.data.skole.elevforhold*.elev*.systemId*.identifikatorverdi == ['E-1', null, 'E-3']
        DISPATCHER.targets.toSet() == [SKOLE, "$COLLECTION?size=500", "$ELEV?size=500",
                                       "$ELEV/systemid/GONE", "$ELEV/systemid/E-3"]*.toString().toSet()
    }

    def "an unconfigured single-valued relation is requested individually above the default threshold"() {
        given:
        def skoler = ['A', 'B', 'C']
        stubSkole(['EF-1', 'EF-2', 'EF-3'])
        DISPATCHER.responses["$COLLECTION?size=500".toString()] = page([0, 1, 2].collect { index ->
            """{"systemId":{"identifikatorverdi":"EF-${index + 1}"},"_links":{
                "self":[{"href":"$BASE$COLLECTION/systemid/EF-${index + 1}"}],
                "skole":[{"href":"$BASE/utdanning/utdanningsprogram/skole/systemid/${skoler[index]}"}]}}"""
        }, null)
        skoler.each { id ->
            DISPATCHER.responses["/utdanning/utdanningsprogram/skole/systemid/$id".toString()] = json("""{"navn":"$id"}""")
        }

        when:
        def response = query('{ skole(systemId: "S") { elevforhold { skole { navn } } } }')

        then:
        response.errors == null
        response.data.skole.elevforhold*.skole*.navn == skoler
        !DISPATCHER.targets.any { it.startsWith('/utdanning/utdanningsprogram/skole?') }
    }

    def "a configured list relation is counted across parents, not per parent"() {
        given: "each klasse is below the threshold of 3, but together they are above it"
        def klasse = '/utdanning/elev/klasse'
        def medlemskap = '/utdanning/elev/klassemedlemskap'
        def entry = { String id ->
            """{"systemId":{"identifikatorverdi":"$id"},"_links":{"self":[{"href":"$BASE$medlemskap/systemid/$id"}]}}"""
        }
        def klasseWith = { String id, List<String> ids ->
            def links = ids.collect { """{"href":"$BASE$medlemskap/systemid/$it"}""" }.join(',')
            json("""{"systemId":{"identifikatorverdi":"$id"},"_links":{"klassemedlemskap":[$links]}}""")
        }
        DISPATCHER.responses[SKOLE] = json("""{"navn":"Skolen","_links":{"klasse":[
                {"href":"$BASE$klasse/systemid/K1"},{"href":"$BASE$klasse/systemid/K2"}]}}""")
        DISPATCHER.responses["$klasse/systemid/K1".toString()] = klasseWith('K1', ['M-1', 'M-2'])
        DISPATCHER.responses["$klasse/systemid/K2".toString()] = klasseWith('K2', ['M-3', 'M-4'])
        DISPATCHER.responses["$medlemskap?size=500".toString()] = page(['M-1', 'M-2', 'M-3', 'M-4'].collect(entry), null)

        when:
        def response = query('{ skole(systemId: "S") { klasse { klassemedlemskap { systemId { identifikatorverdi } } } } }')

        then:
        response.errors == null
        response.data.skole.klasse*.klassemedlemskap*.systemId*.identifikatorverdi == [['M-1', 'M-2'], ['M-3', 'M-4']]
        DISPATCHER.targets.count("$medlemskap?size=500".toString()) == 1
        !DISPATCHER.targets.any { it.startsWith("$medlemskap/systemid/") }
    }

    private Map query(String query) {
        client.post().uri('/graphql')
                .header('Authorization', TestJwtTokens.bearerWithRoles('FINT_Client_UtdanningElev', 'FINT_Client_UtdanningUtdanningsprogram'))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: query])
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map)
                .returnResult()
                .responseBody
    }

    private static void stubSkole(List<String> elevforholdIds) {
        def links = elevforholdIds.collect { """{"href":"$BASE$COLLECTION/systemid/$it"}""" }.join(',')
        DISPATCHER.responses[SKOLE] = json("""{"navn":"Skolen","_links":{"elevforhold":[$links]}}""")
    }

    private static String elevforhold(String id, String elevId) {
        """{"systemId":{"identifikatorverdi":"$id"},"_links":{
            "self":[{"href":"$BASE$COLLECTION/systemid/$id"}],
            "elev":[{"href":"$BASE$ELEV/systemid/$elevId"}]}}"""
    }

    private static String elev(String id) {
        """{"systemId":{"identifikatorverdi":"$id"},"_links":{"self":[{"href":"$BASE$ELEV/systemid/$id"}]}}"""
    }

    private static MockResponse page(List<String> entries, String next) {
        def links = next ? """"next":[{"href":"$BASE$next"}]""" : ''
        json("""{"_embedded":{"_entries":[${entries.join(',')}]},"_links":{$links}}""")
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
