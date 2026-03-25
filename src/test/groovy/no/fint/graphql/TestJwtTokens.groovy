package no.fint.graphql

import com.fasterxml.jackson.databind.ObjectMapper

import java.nio.charset.StandardCharsets
import java.util.Base64

final class TestJwtTokens {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding()
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()

    private TestJwtTokens() {
    }

    static String bearerWithRoles(String... roles) {
        def header = encode([alg: "HS256", typ: "JWT"])
        def payload = encode([sub: "test-client", roles: roles.toList()])
        def signature = URL_ENCODER.encodeToString("signature".getBytes(StandardCharsets.UTF_8))
        return "Bearer ${header}.${payload}.${signature}"
    }

    private static String encode(Map<String, ?> value) {
        return URL_ENCODER.encodeToString(OBJECT_MAPPER.writeValueAsString(value).getBytes(StandardCharsets.UTF_8))
    }
}
