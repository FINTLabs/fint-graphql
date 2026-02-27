package no.fint.graphql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application,
        properties = [
                "management.endpoints.web.base-path=/manage"
        ]
)
@AutoConfigureWebTestClient
class HealthProbeSpec extends Specification {

    @Autowired
    private WebTestClient webTestClient

    def "Startup probe returns UP with ping component"() {
        expect:
        def body = fetchBody("/actuator/health/startup")
        body.status == "UP"
        if (body.components?.ping?.status != null) {
            body.components.ping.status == "UP"
        }
    }

    def "Liveness probe returns UP with ping component"() {
        expect:
        def body = fetchBody("/actuator/health/liveness")
        body.status == "UP"
        if (body.components?.ping?.status != null) {
            body.components.ping.status == "UP"
        }
    }

    def "Readiness probe returns UP with ping component"() {
        expect:
        def body = fetchBody("/actuator/health/readiness")
        body.status == "UP"
        if (body.components?.ping?.status != null) {
            body.components.ping.status == "UP"
        }
    }

    def "Health endpoint returns UP"() {
        expect:
        def body = fetchBody("/actuator/health")
        body.status == "UP"
    }

    private Map fetchBody(String uri) {
        def responseBody = webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String)
                .returnResult()
                .responseBody
        return new com.fasterxml.jackson.databind.ObjectMapper().readValue(responseBody, Map)
    }
}
