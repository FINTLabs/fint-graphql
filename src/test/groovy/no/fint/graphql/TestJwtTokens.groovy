package no.fint.graphql

import com.fasterxml.jackson.databind.ObjectMapper

import java.nio.charset.StandardCharsets
import java.util.Base64

final class TestJwtTokens {

    static final String DEFAULT_ISSUER = "https://idp.felleskomponent.no/nidp/oauth/nam"
    static final String DEFAULT_ORG_ID = "org-123"

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding()
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()

    private TestJwtTokens() {
    }

    static String bearerWithRoles(String... roles) {
        return bearerWithClaims([roles: roles.toList(), fintAssetIDs: DEFAULT_ORG_ID])
    }

    static String bearerWithClaims(Map<String, ?> claims) {
        def payloadClaims = new LinkedHashMap<String, Object>()
        payloadClaims.put("iss", DEFAULT_ISSUER)
        payloadClaims.put("sub", "test-client")
        payloadClaims.putAll(claims)

        def header = encode([alg: "HS256", typ: "JWT"])
        def payload = encode(payloadClaims)
        def signature = URL_ENCODER.encodeToString("signature".getBytes(StandardCharsets.UTF_8))
        return "Bearer ${header}.${payload}.${signature}"
    }

    private static String encode(Map<String, ?> value) {
        return URL_ENCODER.encodeToString(OBJECT_MAPPER.writeValueAsString(value).getBytes(StandardCharsets.UTF_8))
    }
}
