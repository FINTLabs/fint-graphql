package no.fint.graphql

import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import no.fint.graphql.config.GraphQLScalarConfig
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneOffset

class GraphQLScalarConfigSpec extends Specification {

    private final GraphQLScalarConfig config = new GraphQLScalarConfig()
    private final GraphQLContext graphQLContext = GraphQLContext.newContext().build()
    private final Locale locale = Locale.ENGLISH
    private final CoercedVariables variables = CoercedVariables.emptyVariables()

    def "date scalar serializes java.util.Date as ISO date"() {
        given:
        def scalar = config.graphQLDateScalar()
        Date date = Date.from(LocalDate.of(2026, 3, 3).atStartOfDay(ZoneOffset.UTC).toInstant())

        expect:
        scalar.getCoercing().serialize(date, graphQLContext, locale) == "2026-03-03"
    }

    def "date scalar parses ISO date string to java.util.Date"() {
        given:
        def scalar = config.graphQLDateScalar()

        when:
        def parsed = scalar.getCoercing().parseValue("2026-03-03", graphQLContext, locale) as Date

        then:
        parsed.toInstant().atZone(ZoneOffset.UTC).toLocalDate() == LocalDate.of(2026, 3, 3)
    }

    def "date scalar rejects unsupported runtime type"() {
        given:
        def scalar = config.graphQLDateScalar()

        when:
        scalar.getCoercing().serialize(123L, graphQLContext, locale)

        then:
        thrown(CoercingSerializeException)
    }

    def "date scalar parse literal expects StringValue"() {
        given:
        def scalar = config.graphQLDateScalar()

        when:
        scalar.getCoercing().parseLiteral(IntValue.newIntValue(BigInteger.ONE).build(), variables, graphQLContext, locale)

        then:
        thrown(CoercingParseLiteralException)
    }

    def "date scalar parse value rejects invalid input string"() {
        given:
        def scalar = config.graphQLDateScalar()

        when:
        scalar.getCoercing().parseValue("not-a-date", graphQLContext, locale)

        then:
        thrown(CoercingParseValueException)
    }

    def "date scalar parses literal string value"() {
        given:
        def scalar = config.graphQLDateScalar()

        when:
        def parsed = scalar.getCoercing().parseLiteral(
                StringValue.newStringValue("2026-03-03").build(),
                variables,
                graphQLContext,
                locale
        ) as Date

        then:
        parsed.toInstant().atZone(ZoneOffset.UTC).toLocalDate() == LocalDate.of(2026, 3, 3)
    }
}
