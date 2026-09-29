package no.fint.graphql

import com.nimbusds.jwt.SignedJWT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder

import java.time.Instant
import java.util.Date

@TestConfiguration
class TestJwtDecoderConfig {

    @Bean
    @Primary
    JwtDecoder testJwtDecoder() {
        return { String token ->
            def signedJwt = SignedJWT.parse(token)
            def claims = new LinkedHashMap<>(signedJwt.JWTClaimsSet.claims)
            def headers = new LinkedHashMap<>(signedJwt.header.toJSONObject())
            Instant issuedAt = claims.get("iat") instanceof Date
                    ? ((Date) claims.get("iat")).toInstant()
                    : claims.get("iat") instanceof Number
                    ? Instant.ofEpochSecond(((Number) claims.get("iat")).longValue())
                    : Instant.now()
            Instant expiresAt = claims.get("exp") instanceof Date
                    ? ((Date) claims.get("exp")).toInstant()
                    : claims.get("exp") instanceof Number
                    ? Instant.ofEpochSecond(((Number) claims.get("exp")).longValue())
                    : issuedAt.plusSeconds(3600)
            new Jwt(token, issuedAt, expiresAt, headers, claims)
        } as JwtDecoder
    }
}
