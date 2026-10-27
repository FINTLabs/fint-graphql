package no.fint.graphql

import tools.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.mock.web.MockHttpSession
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration
import java.time.Instant

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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

    @Autowired WebApplicationContext applicationContext
    @Autowired FilterChainProxy springSecurityFilterChain

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

    def "unsafe requests outside GraphQL retain CSRF protection"() {
        given:
        def mvc = MockMvcBuilders.webAppContextSetup(applicationContext).addFilters(springSecurityFilterChain).build()

        expect:
        mvc.perform(post('/schema.json').contentType(MediaType.APPLICATION_JSON).content('{}'))
                .andExpect(status().isForbidden())
    }

    def "GraphQL accepts bearer tokens without a CSRF token or session"() {
        expect:
        webTestClient.post().uri('/graphql')
                .header('Authorization', TestJwtTokens.bearerWithRoles('FINT_Client_AdministrasjonFullmakt'))
                .contentType(MediaType.APPLICATION_JSON).bodyValue([query: '{ __typename }'])
                .exchange().expectStatus().isOk()
                .expectHeader().doesNotExist('Set-Cookie')
                .expectBody().jsonPath('$.data.__typename').isEqualTo('Query')
    }

    def "GraphQL never authenticates using an existing HTTP session"() {
        given:
        def jwt = new Jwt('session-token', Instant.now(), Instant.now().plusSeconds(60),
                [alg: 'HS256'], [sub: 'test-client', fintAssetIDs: TestJwtTokens.DEFAULT_ORG_ID])
        def session = new MockHttpSession(applicationContext.servletContext)
        session.setAttribute('SPRING_SECURITY_CONTEXT', new SecurityContextImpl(new JwtAuthenticationToken(jwt, [])))
        def mvc = MockMvcBuilders.webAppContextSetup(applicationContext).addFilters(springSecurityFilterChain).build()

        expect:
        session.getAttribute('SPRING_SECURITY_CONTEXT').authentication.authenticated
        mvc.perform(post('/graphql').session(session).contentType(MediaType.APPLICATION_JSON)
                .content('{"query":"{ __typename }"}')).andExpect(status().isUnauthorized())
    }
}
