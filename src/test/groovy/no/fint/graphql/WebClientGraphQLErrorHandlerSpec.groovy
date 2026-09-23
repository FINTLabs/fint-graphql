package no.fint.graphql

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import graphql.ErrorType
import graphql.ExceptionWhileDataFetching
import graphql.execution.ResultPath
import graphql.language.SourceLocation
import graphql.validation.ValidationError
import graphql.validation.ValidationErrorType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClientResponseException
import spock.lang.Specification

class WebClientGraphQLErrorHandlerSpec extends Specification {
    def handler = new WebClientGraphQLErrorHandler()
    Logger logger = (Logger) LoggerFactory.getLogger(WebClientGraphQLErrorHandler)
    def appender = new ListAppender<ILoggingEvent>()

    def setup() {
        appender.start()
        logger.addAppender(appender)
    }

    def cleanup() {
        logger.detachAppender(appender)
        appender.stop()
    }

    def "already mapped #failure.class.simpleName retains its response without an unmapped warning"() {
        given:
        def original = new ExceptionWhileDataFetching(ResultPath.fromList(['person', 'foreldre', 1]),
                failure, new SourceLocation(8, 4))
        def mapped = handler.processErrors([original]).first()
        def response = mapped.toSpecification()
        appender.list.clear()

        when:
        def again = handler.processErrors([mapped]).first()

        then:
        !mapped.is(original)
        again.is(mapped)
        again.toSpecification() == response
        again.path == ['person', 'foreldre', 1]
        again.locations == [new SourceLocation(8, 4)]
        appender.list.empty

        where:
        failure << [
                new MissingAuthorizationException('Missing token'),
                new UnauthorizedResourceAccessException('Forbidden', '/utdanning/elev/person/fodselsnummer/1', '1', '2'),
                WebClientResponseException.create(404, 'Not Found', HttpHeaders.EMPTY, new byte[0], null),
                new WebClientRequestException('Connection failed', new IOException('Connection refused'),
                        '/utdanning/elev/person/fodselsnummer/1', '1', '2')
        ]
    }

    def "undefined field validation retains its details and gains stable extensions"() {
        given:
        def original = ValidationError.newValidationError()
                .validationErrorType(ValidationErrorType.FieldUndefined)
                .description("Field 'basisgruppemedlemskap' in type 'Elevforhold' is undefined")
                .queryPath(['skole', 'elevforhold', 'basisgruppemedlemskap'])
                .sourceLocation(new SourceLocation(8, 4))
                .extensions([requestId: '123'])
                .build()

        when:
        def mapped = handler.processErrors([original]).first()

        then:
        mapped.message == original.message
        mapped.locations == original.locations
        mapped.path == null
        mapped.errorType == ErrorType.ValidationError
        mapped.extensions == [requestId: '123', code: 'GRAPHQL_VALIDATION_FAILED',
                              validationErrorType: 'FieldUndefined',
                              queryPath: ['skole', 'elevforhold', 'basisgruppemedlemskap']]
        original.extensions == [requestId: '123']
        handler.processErrors([mapped]).first().is(mapped)
        appender.list.empty
    }

    def "validation mapping preserves an existing custom error code without adding an empty query path"() {
        given:
        def original = ValidationError.newValidationError()
                .validationErrorType(ValidationErrorType.WrongType)
                .description('Invalid argument type')
                .extensions([code: 'INVALID_ARGUMENT', extra: 'detail'])
                .build()

        when:
        def mapped = handler.processErrors([original]).first()

        then:
        mapped.extensions == [code: 'INVALID_ARGUMENT', extra: 'detail', validationErrorType: 'WrongType']
        mapped.errorType == ErrorType.ValidationError
        appender.list.empty
    }
}
