package no.fint.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import java.time.Duration

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = no.fint.graphql.Application,
        properties = [
                "spring.main.allow-bean-definition-overriding=true",
                "fint.graphql.blacklist=127.0.0.1,::1"
        ]
)
@AutoConfigureWebTestClient
class GraphQLAuthBlacklistedSpec extends Specification {

    @Autowired
    private WebTestClient webTestClient

    def "Blacklisted IP returns 401 with GraphQL error body"() {
        given:
        def query = 'query { rolle(navn: "foo") { navn { identifikatorverdi } } }'

        when:
        def responseBody = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(5))
                .build()
                .post()
                .uri("/graphql")
                .header(HttpHeaders.AUTHORIZATION, "Bearer header.payload.signature")
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
