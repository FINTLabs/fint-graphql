package no.fint.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, TestJwtDecoderConfig],
        properties = [
                "spring.main.allow-bean-definition-overriding=true",
                "fint.graphql.blacklist="
        ]
)
@ContextConfiguration
@AutoConfigureWebTestClient
class ActuatorProbeSecuritySpec extends Specification {

    @Autowired
    private WebTestClient webTestClient

    @Unroll
    def "Probe endpoint #path is accessible without authentication"() {
        when:
        def responseBody = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(5))
                .build()
                .get()
                .uri(path)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String)
                .responseBody
                .blockFirst()

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.status in ["UP", "OUT_OF_SERVICE", "DOWN", "UNKNOWN"]

        where:
        path << [
                "/actuator/health"
        ]
    }

    def "GraphQL endpoint still requires authentication"() {
        given:
        def query = 'query { rolle(navn: "foo") { navn { identifikatorverdi } } }'

        when:
        def responseBody = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(5))
                .build()
                .post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: query])
                .exchange()
                .expectStatus().isUnauthorized()
                .returnResult(String)
                .responseBody
                .blockFirst()

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data == null
        body.errors?.size() == 1
        body.errors[0].extensions?.code == 401
        body.errors[0].message == "Unauthorized"
    }
}
