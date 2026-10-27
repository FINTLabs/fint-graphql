package no.fint.graphql.config

import no.fint.graphql.TestJwtTokens
import spock.lang.Specification

class JwtRolePathPrefixExtractorSpec extends Specification {

    private final JwtRolePathPrefixExtractor extractor = new JwtRolePathPrefixExtractor()

    def "extracts unique path prefixes from FINT client roles in multiple formats"() {
        given:
        def authorization = TestJwtTokens.bearerWithRoles(
                "FINT_Client_AdministrasjonFullmakt",
                "FINT_Client_administrasjon_fullmakt",
                "FINT_Client_UtdanningUtdanningsprogram",
                "FINT_Client_utdanning_utdanningsprogram",
                "authenticated"
        )

        when:
        def prefixes = extractor.extractAllowedPathPrefixes(authorization)

        then:
        prefixes == [
                "/administrasjon/fullmakt/",
                "/utdanning/utdanningsprogram/"
        ] as Set
    }

    def "extracts /felles/kodeverk/ from both supported felles kodeverk role formats"() {
        given:
        def authorization = TestJwtTokens.bearerWithRoles(
                "FINT_Client_FellesKodeverk",
                "FINT_Client_felles_kodeverk"
        )

        when:
        def prefixes = extractor.extractAllowedPathPrefixes(authorization)

        then:
        prefixes == ["/felles/kodeverk/"] as Set
    }
}
