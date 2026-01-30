package no.fint.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApplication)
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

    def "GraphQL maps status #status to expected error object"() {
        given:
        server.enqueue(new MockResponse().setResponseCode(status).setBody("error"))
        def query = 'query { rolle(navn: "foo") { navn { identifikatorverdi } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data?.rolle == null
        body.errors?.size() == 1
        body.errors[0].path == ["rolle"]
        body.errors[0].message == expectedMessage(status)
        body.errors[0].extensions?.classification == "DataFetchingException"

        where:
        status << [401, 403, 404]
    }

    def "GraphQL returns message and path for status #status"() {
        given:
        server.enqueue(new MockResponse().setResponseCode(status).setBody("error"))
        def query = 'query { rolle(navn: "foo") { navn { identifikatorverdi } } }'

        when:
        def responseBody = executeQuery(query)

        then:
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.errors?.size() == 1
        body.errors[0].message == expectedMessage(status)
        body.errors[0].path == ["rolle"]

        where:
        status << [401, 403, 404]
    }

    private static String expectedMessage(int status) {
        def resourcePath = "/administrasjon/fullmakt/rolle/navn/foo"
        if (status == 401) {
            return "Access unauthorized for ${resourcePath}"
        }
        if (status == 403) {
            return "Access forbidden for ${resourcePath}"
        }
        return "Failed to find resource ${resourcePath}"
    }

    private String executeQuery(String query) {
        return webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(20))
                .build()
                .post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: query])
                .exchange()
                .expectStatus().isOk()
                .returnResult(String)
                .responseBody
                .blockFirst()
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

    @SpringBootConfiguration
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
    }
}
