package no.fint.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, SignatureValidationJwtDecoderConfig],
        properties = [
                "spring.main.allow-bean-definition-overriding=true",
                "fint.graphql.blacklist="
        ]
)
@ContextConfiguration
@AutoConfigureWebTestClient
class GraphQLAuthUnsignedTokenSpec extends Specification {

    @Autowired
    private WebTestClient webTestClient

    def "Unsigned access token returns 401 with WWW-Authenticate and GraphQL error body"() {
        given:
        def query = 'query { rolle(navn: "foo") { navn { identifikatorverdi } } }'
        def token = unsignedBearerToken(
                [
                        iss         : TestJwtTokens.DEFAULT_ISSUER,
                        sub         : "unsigned-client",
                        roles       : ["FINT_Client_AdministrasjonFullmakt"],
                        fintAssetIDs: TestJwtTokens.DEFAULT_ORG_ID,
                        exp         : Instant.now().plusSeconds(3600).epochSecond
                ]
        )

        when:
        def response = webTestClient.mutate()
                .responseTimeout(Duration.ofSeconds(5))
                .build()
                .post()
                .uri("/graphql")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: query])
                .exchange()
                .expectStatus().isUnauthorized()
                .returnResult(String)

        then:
        def wwwAuthenticate = response.responseHeaders.getFirst(HttpHeaders.WWW_AUTHENTICATE)
        wwwAuthenticate != null
        wwwAuthenticate.startsWith("Bearer")
        wwwAuthenticate.contains('invalid_token')

        and:
        def responseBody = response.responseBody.blockFirst()
        def body = new ObjectMapper().readValue(responseBody, Map)
        body.data == null
        body.errors?.size() == 1
        body.errors[0].extensions?.code == 401
        body.errors[0].message == "Unauthorized"
    }

    private static String unsignedBearerToken(Map<String, ?> claims) {
        def header = encode([alg: "none", typ: "JWT"])
        def payload = encode(claims)
        return "Bearer ${header}.${payload}."
    }

    private static String encode(Map<String, ?> value) {
        def json = new ObjectMapper().writeValueAsString(value)
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(json.getBytes(StandardCharsets.UTF_8))
    }

    @TestConfiguration
    static class SignatureValidationJwtDecoderConfig {

        @Bean
        @Primary
        JwtDecoder testJwtDecoder() {
            def secret = new SecretKeySpec(
                    "0123456789abcdef0123456789abcdef".getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            )
            return NimbusJwtDecoder.withSecretKey(secret)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build()
        }
    }
}
