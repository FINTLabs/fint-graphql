package no.fint.graphql

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.time.Duration
import java.util.concurrent.TimeUnit

/** The shape of a real query: two schools, several relations per elevforhold, person several levels down. */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, TestJwtDecoderConfig],
        properties = ['fint.graphql.blacklist=']
)
@AutoConfigureWebTestClient
class RelationPaginationDeepQuerySpec extends Specification {
    private static final String BASE = 'https://beta.felleskomponent.no'
    private static final String SKOLE = '/utdanning/utdanningsprogram/skole'
    private static final String ELEVFORHOLD = '/utdanning/elev/elevforhold'
    private static final String ELEV = '/utdanning/elev/elev'
    private static final String PERSON = '/utdanning/elev/person'
    private static final String MEDLEMSKAP = '/utdanning/elev/klassemedlemskap'
    private static final String KLASSE = '/utdanning/elev/klasse'
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

    def "person several levels down a two school query is read from pages, not individually"() {
        given:
        DISPATCHER.targets.clear()
        DISPATCHER.responses.clear()
        def ids = (1..120).collect { it.toString() }
        def first = ids.take(70)
        def second = ids.drop(70)
        [['39018', first], ['39020', second]].each { number, group ->
            def links = group.collect { """{"href":"$BASE$ELEVFORHOLD/systemid/EF-$it"}""" }.join(',')
            DISPATCHER.responses["$SKOLE/skolenummer/$number".toString()] = json(
                    """{"navn":"$number","skolenummer":{"identifikatorverdi":"$number"},"_links":{"elevforhold":[$links]}}""")
        }
        DISPATCHER.responses["$ELEVFORHOLD?size=500".toString()] = page(ids.collect { id ->
            """{"systemId":{"identifikatorverdi":"EF-$id"},"hovedskole":true,"_links":{
                "self":[{"href":"$BASE$ELEVFORHOLD/systemid/EF-$id"}],
                "elev":[{"href":"$BASE$ELEV/systemid/E-$id"}],
                "klassemedlemskap":[{"href":"$BASE$MEDLEMSKAP/systemid/M-$id"}]}}"""
        })
        DISPATCHER.responses["$ELEV?size=500".toString()] = page(ids.collect { id ->
            """{"systemId":{"identifikatorverdi":"E-$id"},"elevnummer":{"identifikatorverdi":"N-$id"},"_links":{
                "self":[{"href":"$BASE$ELEV/systemid/E-$id"},{"href":"$BASE$ELEV/elevnummer/N-$id"}],
                "person":[{"href":"$BASE$PERSON/fodselsnummer/$id"}]}}"""
        })
        DISPATCHER.responses["$MEDLEMSKAP?size=500".toString()] = page(ids.collect { id ->
            """{"systemId":{"identifikatorverdi":"M-$id"},"_links":{
                "self":[{"href":"$BASE$MEDLEMSKAP/systemid/M-$id"}],
                "klasse":[{"href":"$BASE$KLASSE/systemid/K-1"}]}}"""
        })
        DISPATCHER.responses["$KLASSE/systemid/K-1".toString()] = json('{"navn":"1A"}')
        DISPATCHER.responses["$PERSON?size=500".toString()] = page(ids.collect { id ->
            """{"fodselsnummer":{"identifikatorverdi":"$id"},"navn":{"fornavn":"F$id","etternavn":"E$id"},"_links":{
                "self":[{"href":"$BASE$PERSON/fodselsnummer/$id"}]}}"""
        })

        when:
        def response = client.mutate().responseTimeout(Duration.ofSeconds(20)).build()
                .post().uri('/graphql')
                .header('Authorization', TestJwtTokens.bearerWithRoles('FINT_Client_UtdanningElev', 'FINT_Client_UtdanningUtdanningsprogram'))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: '''
                    query {
                      sandefjord: skole(skolenummer: "39018") { ...elevdata }
                      thorHeyerdahl: skole(skolenummer: "39020") { ...elevdata }
                    }
                    fragment elevdata on Skole {
                      navn
                      skolenummer { identifikatorverdi }
                      elevforhold {
                        hovedskole
                        klassemedlemskap { klasse { navn } }
                        elev {
                          elevnummer { identifikatorverdi }
                          person {
                            fodselsnummer { identifikatorverdi }
                            navn { fornavn etternavn }
                          }
                        }
                      }
                    }'''])
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map)
                .returnResult()
                .responseBody
        def targets = DISPATCHER.targets

        then:
        response.errors == null
        response.data.sandefjord.elevforhold*.elev*.person*.fodselsnummer*.identifikatorverdi == first
        response.data.thorHeyerdahl.elevforhold*.elev*.person*.fodselsnummer*.identifikatorverdi == second
        !targets.any { it.startsWith("$PERSON/") }
        !targets.any { it.startsWith("$ELEV/") }
        targets.count("$PERSON?size=500".toString()) == 1
    }

    def "person is still one walk when parents and pages respond at different speeds"() {
        given:
        DISPATCHER.targets.clear()
        DISPATCHER.responses.clear()
        def ids = (1..120).collect { it.toString() }
        def first = ids.take(70)
        def second = ids.drop(70)
        [['39018', first, 0], ['39020', second, 700]].each { number, group, delay ->
            def links = group.collect { """{"href":"$BASE$ELEVFORHOLD/systemid/EF-$it"}""" }.join(',')
            DISPATCHER.responses["$SKOLE/skolenummer/$number".toString()] = json(
                    """{"navn":"$number","_links":{"elevforhold":[$links]}}""", delay)
        }
        DISPATCHER.responses["$ELEVFORHOLD?size=500".toString()] = page(ids.collect { id ->
            """{"systemId":{"identifikatorverdi":"EF-$id"},"_links":{
                "self":[{"href":"$BASE$ELEVFORHOLD/systemid/EF-$id"}],
                "elev":[{"href":"$BASE$ELEV/systemid/E-$id"}]}}"""
        }, 300)
        def elevEntry = { String id ->
            """{"systemId":{"identifikatorverdi":"E-$id"},"_links":{
                "self":[{"href":"$BASE$ELEV/systemid/E-$id"}],
                "person":[{"href":"$BASE$PERSON/fodselsnummer/$id"}]}}"""
        }
        // Ten elever are missing from the pages, so their parents finish much later than the rest.
        DISPATCHER.responses["$ELEV?size=500".toString()] = page(ids.drop(10).collect(elevEntry), 300)
        ids.take(10).each { id ->
            DISPATCHER.responses["$ELEV/systemid/E-$id".toString()] = json(elevEntry(id), 500)
        }
        DISPATCHER.responses["$PERSON?size=500".toString()] = page(ids.collect { id ->
            """{"fodselsnummer":{"identifikatorverdi":"$id"},"navn":{"fornavn":"F$id","etternavn":"E$id"},"_links":{
                "self":[{"href":"$BASE$PERSON/fodselsnummer/$id"}]}}"""
        }, 300)

        when:
        def response = client.mutate().responseTimeout(Duration.ofSeconds(30)).build()
                .post().uri('/graphql')
                .header('Authorization', TestJwtTokens.bearerWithRoles('FINT_Client_UtdanningElev', 'FINT_Client_UtdanningUtdanningsprogram'))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: '''
                    query {
                      sandefjord: skole(skolenummer: "39018") { ...elevdata }
                      thorHeyerdahl: skole(skolenummer: "39020") { ...elevdata }
                    }
                    fragment elevdata on Skole {
                      elevforhold { elev { person { fodselsnummer { identifikatorverdi } } } }
                    }'''])
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map)
                .returnResult()
                .responseBody
        def targets = DISPATCHER.targets

        then:
        response.errors == null
        response.data.sandefjord.elevforhold*.elev*.person*.fodselsnummer*.identifikatorverdi == first
        response.data.thorHeyerdahl.elevforhold*.elev*.person*.fodselsnummer*.identifikatorverdi == second
        !targets.any { it.startsWith("$PERSON/") }
        targets.count("$PERSON?size=500".toString()) == 1
    }

    private static MockResponse page(List<String> entries, long delayMillis = 0) {
        json("""{"_embedded":{"_entries":[${entries.join(',')}]},"_links":{},"total_items":${entries.size()}}""", delayMillis)
    }

    private static MockResponse json(String body, long delayMillis = 0) {
        new MockResponse.Builder().code(200).setHeader(HttpHeaders.CONTENT_TYPE, 'application/json')
                .body(body).bodyDelay(delayMillis, TimeUnit.MILLISECONDS).build()
    }
}
