package no.fint.graphql.config

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

class GraphQLQueryTimeoutConfigSpec extends Specification {
    @Unroll
    def "effective timeout for query #query and servlet #servlet is #expected"() {
        expect:
        GraphQLQueryTimeoutConfig.effectiveTimeoutMillis(Duration.ofMillis(query), Duration.ofMillis(servlet)) == expected

        where:
        query | servlet | expected
        110   | 115     | 110
        200   | 100     | 100
        0     | 100     | 100
        100   | 0       | 100
        -1    | 100     | 100
        100   | -1      | 100
        0     | 0       | 0
    }
}
