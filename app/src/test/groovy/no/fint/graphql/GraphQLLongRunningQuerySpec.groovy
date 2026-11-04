package no.fint.graphql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.time.Duration

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, GraphQLQueryTimeoutSpec.TimeoutTestConfig, TestJwtDecoderConfig],
        properties = [
                'spring.profiles.active=timeout-test',
                'spring.main.allow-bean-definition-overriding=true',
                'spring.threads.virtual.enabled=true',
                'fint.graphql.query-timeout=PT35S',
                'fint.graphql.async-request-timeout=PT40S',
                'test.resolver-delay=PT31S'
        ]
)
@ContextConfiguration
@AutoConfigureWebTestClient
class GraphQLLongRunningQuerySpec extends Specification {
    @Autowired
    private WebTestClient webTestClient

    @Autowired
    private ServletWebServerApplicationContext applicationContext

    def "query completes beyond Tomcat's default 30 second async timeout"() {
        expect:
        applicationContext.webServer.tomcat.connector.asyncTimeout == 40000
        webTestClient.mutate().responseTimeout(Duration.ofSeconds(40)).build()
                .post().uri('/graphql')
                .header('Authorization', TestJwtTokens.bearerWithRoles('FINT_Client_AdministrasjonFullmakt'))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: '{ rolle(navn: "slow") { navn { identifikatorverdi } } }'])
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath('$.errors').doesNotExist()
                .jsonPath('$.data.rolle.navn.identifikatorverdi').isEqualTo('SLOW')
    }
}
