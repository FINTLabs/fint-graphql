package no.fint.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import graphql.schema.DataFetchingEnvironment
import no.fint.graphql.model.model.rolle.RolleService
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.administrasjon.fullmakt.RolleResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, TimeoutTestConfig],
        properties = [
                "spring.profiles.active=timeout-test",
                "spring.main.allow-bean-definition-overriding=true",
                "graphql.servlet.async-mode-enabled=true",
                "fint.graphql.query-timeout=PT0.2S",
                "fint.graphql.async-request-timeout=PT2S"
        ]
)
@AutoConfigureWebTestClient
class GraphQLQueryTimeoutSpec extends Specification {

    @Autowired
    private WebTestClient webTestClient

    def "GraphQL query-timeout returns timeout error within a few seconds"() {
        given:
        def query = 'query { rolle(navn: "slow") { navn { identifikatorverdi } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data == null
        body.errors?.size() == 1
        body.errors[0].extensions?.code == "QUERY_TIMEOUT"
        body.errors[0].message?.contains("timed out")
    }

    private String executeQuery(String query) {
        return webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(5))
                .build()
                .post()
                .uri("/graphql")
                .header("Authorization", "Bearer header.payload.signature")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: query])
                .exchange()
                .expectStatus().isOk()
                .returnResult(String)
                .responseBody
                .blockFirst()
    }

    @TestConfiguration
    @Profile("timeout-test")
    static class TimeoutTestConfig {
        @Bean
        @Primary
        RolleService slowRolleService() {
            return new RolleService() {
                @Override
                Mono<RolleResource> getRolleResourceById(String id, String value, DataFetchingEnvironment dfe) {
                    if (!"slow".equals(value)) {
                        return Mono.empty()
                    }
                    def rolle = new RolleResource()
                    def navn = new Identifikator()
                    navn.setIdentifikatorverdi("SLOW")
                    rolle.setNavn(navn)
                    return Mono.delay(Duration.ofSeconds(1)).thenReturn(rolle)
                }
            }
        }
    }
}
