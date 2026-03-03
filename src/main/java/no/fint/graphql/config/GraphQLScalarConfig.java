package no.fint.graphql.config;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.scalars.ExtendedScalars;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Date;
import java.util.Locale;

@Configuration
public class GraphQLScalarConfig {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final ZoneId DATE_ZONE = ZoneOffset.UTC;

    @Bean
    public GraphQLScalarType graphQLLongScalar() {
        return ExtendedScalars.GraphQLLong;
    }

    @Bean
    public GraphQLScalarType graphQLDateScalar() {
        return GraphQLScalarType.newScalar()
                .name("Date")
                .description("ISO-8601 date scalar backed by java.util.Date")
                .coercing(new Coercing<Date, String>() {
                    @Override
                    public String serialize(Object input, GraphQLContext graphQLContext, Locale locale)
                            throws CoercingSerializeException {
                        if (input instanceof Date date) {
                            return formatDate(date);
                        }
                        if (input instanceof String stringValue) {
                            try {
                                return formatDate(parseDate(stringValue));
                            } catch (CoercingParseValueException ex) {
                                throw new CoercingSerializeException(ex.getMessage(), ex);
                            }
                        }
                        if (input instanceof TemporalAccessor temporalAccessor) {
                            return formatTemporalAccessor(temporalAccessor);
                        }
                        throw new CoercingSerializeException(
                                "Expected a 'String', 'java.util.Date' or 'java.time.temporal.TemporalAccessor' but was '"
                                        + typeName(input) + "'."
                        );
                    }

                    @Override
                    public Date parseValue(Object input, GraphQLContext graphQLContext, Locale locale)
                            throws CoercingParseValueException {
                        if (input instanceof Date date) {
                            return date;
                        }
                        if (input instanceof String stringValue) {
                            return parseDate(stringValue);
                        }
                        if (input instanceof TemporalAccessor temporalAccessor) {
                            return parseTemporalAccessor(temporalAccessor);
                        }
                        throw new CoercingParseValueException(
                                "Expected a 'String', 'java.util.Date' or 'java.time.temporal.TemporalAccessor' but was '"
                                        + typeName(input) + "'."
                        );
                    }

                    @Override
                    public Date parseLiteral(
                            Value<?> input,
                            CoercedVariables variables,
                            GraphQLContext graphQLContext,
                            Locale locale
                    ) throws CoercingParseLiteralException {
                        if (input instanceof StringValue stringValue) {
                            try {
                                return parseDate(stringValue.getValue());
                            } catch (CoercingParseValueException ex) {
                                throw new CoercingParseLiteralException(ex.getMessage(), ex);
                            }
                        }
                        throw new CoercingParseLiteralException(
                                "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                        );
                    }

                    @Override
                    public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                        return StringValue.newStringValue(serialize(input, graphQLContext, locale)).build();
                    }
                })
                .build();
    }

    private static Date parseDate(String value) {
        try {
            LocalDate localDate = LocalDate.parse(value, DATE_FORMATTER);
            return Date.from(localDate.atStartOfDay(DATE_ZONE).toInstant());
        } catch (DateTimeParseException ex) {
            throw new CoercingParseValueException("Invalid ISO-8601 date value '" + value + "'.", ex);
        }
    }

    private static String formatDate(Date date) {
        return date.toInstant()
                .atZone(DATE_ZONE)
                .toLocalDate()
                .format(DATE_FORMATTER);
    }

    private static String formatTemporalAccessor(TemporalAccessor temporalAccessor) {
        return extractLocalDate(temporalAccessor)
                .format(DATE_FORMATTER);
    }

    private static Date parseTemporalAccessor(TemporalAccessor temporalAccessor) {
        try {
            LocalDate localDate = extractLocalDate(temporalAccessor);
            return Date.from(localDate.atStartOfDay(DATE_ZONE).toInstant());
        } catch (CoercingSerializeException ex) {
            throw new CoercingParseValueException(ex.getMessage(), ex);
        }
    }

    private static LocalDate extractLocalDate(TemporalAccessor temporalAccessor) {
        LocalDate localDate = temporalAccessor.query(TemporalQueries.localDate());
        if (localDate != null) {
            return localDate;
        }
        if (temporalAccessor instanceof Instant instant) {
            return instant.atZone(DATE_ZONE).toLocalDate();
        }
        throw new CoercingSerializeException(
                "Unable to extract LocalDate from TemporalAccessor type '" + temporalAccessor.getClass().getSimpleName() + "'."
        );
    }

    private static String typeName(Object value) {
        return value == null ? "null" : value.getClass().getSimpleName();
    }
}
