package no.fint.graphql

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.OutputStreamAppender
import ch.qos.logback.core.read.ListAppender
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import mockwebserver3.RecordedRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper

import java.time.Duration
import java.util.concurrent.TimeUnit

/** Sends real requests through the application and checks the log line in the production JSON format. */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, TestJwtDecoderConfig],
        properties = ['fint.graphql.blacklist=', 'fint.graphql.query-timeout=PT1S']
)
@AutoConfigureWebTestClient
class GraphQLQueryLogSpec extends Specification {
    private static final String FODSELSNUMMER = '12345678901'
    private static final FixedDispatcher DISPATCHER = new FixedDispatcher()
    private static final MockWebServer SERVER = startServer()

    @Autowired
    WebTestClient client

    Logger logger = (Logger) LoggerFactory.getLogger(GraphQLRequestInterceptor)
    def appender = new ListAppender<ILoggingEvent>()

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add('fint.endpoint.root', { SERVER.url('/').toString() })
    }

    def setup() {
        appender.start()
        logger.addAppender(appender)
    }

    def cleanup() {
        logger.detachAppender(appender)
        appender.stop()
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

    def "a query is logged with its client, duration and shape, without its values"() {
        given:
        DISPATCHER.response = person()

        when:
        post(bearer(cn: 'client@org.no'), """query Oppslag { a: person(fodselsnummer: "$FODSELSNUMMER") { navn { fornavn } } }""")
        def line = completedLine()

        then:
        line.path('client').asString() == 'client@org.no'
        line.path('organisation').asString() == TestJwtTokens.DEFAULT_ORG_ID
        line.path('outcome').asString() == 'ok'
        line.path('operation').asString() == 'Oppslag'
        line.path('durationMs').isNumber()
        line.path('requestCount').asLong() == 2 // person is read from both person sources
        line.path('queryUid').asString().length() == 36
        line.path('queryHash').asString().length() == 16
        line.path('query').asString().contains('person(fodselsnummer:"")')
        !line.toString().contains(FODSELSNUMMER)
        line.path('message').asString().startsWith('GraphQL query completed queryId=')
    }

    def "variable values are not logged"() {
        given:
        DISPATCHER.response = person()

        when:
        post(bearer(cn: 'client@org.no'), 'query($id: String) { person(fodselsnummer: $id) { navn { fornavn } } }',
                [id: FODSELSNUMMER])

        then:
        !completedLine().toString().contains(FODSELSNUMMER)
    }

    def "the subject names the client when the token has no cn"() {
        given:
        DISPATCHER.response = person()

        when:
        post(bearer([:]), '{ person(fodselsnummer: "1") { navn { fornavn } } }')

        then:
        completedLine().path('client').asString() == 'test-client'
    }

    def "the x-client header cannot change the logged client"() {
        given:
        DISPATCHER.response = person()

        when:
        client.post().uri('/graphql')
                .header(HttpHeaders.AUTHORIZATION, bearer(cn: 'client@org.no'))
                .header(GraphQLRequestAttributes.CLIENT_HEADER, 'someone-else')
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: '{ person(fodselsnummer: "1") { navn { fornavn } } }'])
                .exchange().expectStatus().isOk()

        then:
        completedLine().path('client').asString() == 'client@org.no'
    }

    def "a failed upstream request is logged as partial or error, not ok"() {
        given:
        DISPATCHER.response = new MockResponse.Builder().code(500).build()

        when:
        post(bearer(cn: 'client@org.no'), '{ person(fodselsnummer: "1") { navn { fornavn } } }')

        then:
        completedLine().path('outcome').asString() in ['partial', 'error']
    }

    def "a query stopped by the query timeout is logged as timeout with its duration"() {
        given:
        DISPATCHER.response = person().newBuilder().headersDelay(5, TimeUnit.SECONDS).build()

        when:
        post(bearer(cn: 'client@org.no'), '{ person(fodselsnummer: "1") { navn { fornavn } } }')
        def line = completedLine()

        then:
        line.path('outcome').asString() == 'timeout'
        line.path('durationMs').asLong() >= 1000
    }

    def "an invalid query is still logged, without query text"() {
        when:
        post(bearer(cn: 'client@org.no'), "{ person(fodselsnummer: \"$FODSELSNUMMER\") { ")
        def line = completedLine()

        then:
        line.path('client').asString() == 'client@org.no'
        line.path('outcome').asString() == 'error'
        line.path('query').isMissingNode()
        !line.toString().contains(FODSELSNUMMER)
    }

    private void post(String authorization, String query, Map variables = null) {
        client.mutate().responseTimeout(Duration.ofSeconds(20)).build()
                .post().uri('/graphql')
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(variables == null ? [query: query] : [query: query, variables: variables])
                .exchange().expectStatus().isOk()
    }

    private static String bearer(Map<String, ?> claims) {
        TestJwtTokens.bearerWithClaims([roles: ['FINT_Client_Utdanning_Elev'], fintAssetIDs: TestJwtTokens.DEFAULT_ORG_ID] + claims)
    }

    private static MockResponse person() {
        new MockResponse.Builder()
                .addHeader('Content-Type', 'application/json')
                .body('{"navn":{"fornavn":"Ola"}}')
                .build()
    }

    /** Formats the event with the appender from logback-spring.xml, since tests log with logback-test.xml. */
    private JsonNode completedLine() {
        def deadline = System.currentTimeMillis() + 5000
        ILoggingEvent event = null
        while (event == null && System.currentTimeMillis() < deadline) {
            event = appender.list.toList().find { it.formattedMessage.startsWith('GraphQL query completed') }
            if (event == null) {
                Thread.sleep(10)
            }
        }
        assert event != null
        new ObjectMapper().readTree(new String(productionEncoder().encode(event), 'UTF-8'))
    }

    private static productionEncoder() {
        def context = new LoggerContext()
        def configurator = new JoranConfigurator()
        configurator.context = context
        new ClassPathResource('logback-spring.xml').inputStream.withCloseable { configurator.doConfigure(it) }
        def console = (OutputStreamAppender) context.getLogger(Logger.ROOT_LOGGER_NAME).getAppender('CONSOLE-JSON')
        console.encoder
    }
}

class FixedDispatcher extends Dispatcher {
    volatile MockResponse response

    @Override
    MockResponse dispatch(RecordedRequest request) {
        response
    }
}
