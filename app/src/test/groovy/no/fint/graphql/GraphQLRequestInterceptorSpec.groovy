package no.fint.graphql

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import reactor.core.publisher.Mono
import spock.lang.Specification

class GraphQLRequestInterceptorSpec extends Specification {
    Logger logger = (Logger) LoggerFactory.getLogger(GraphQLRequestInterceptor)
    def appender = new ListAppender<ILoggingEvent>()

    def setup() {
        appender.start()
        logger.addAppender(appender)
    }

    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
        logger.detachAppender(appender)
        appender.stop()
    }

    def "request logging prevents forged lines without changing request values"() {
        given:
        def servletRequest = new MockHttpServletRequest('POST', '/graphql\r\nFORGED')
        servletRequest.addHeader(GraphQLRequestAttributes.CLIENT_HEADER, 'client\nFORGED\u0085\u2028\u2029')
        servletRequest.remoteAddr = '127.0.0.1\rFORGED'
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest))
        def request = new WebGraphQlRequest(URI.create('/graphql'), new HttpHeaders(),
                new LinkedMultiValueMap(), [:], [query: '{ __typename }'], 'request', Locale.ROOT)
        def chain = Mock(WebGraphQlInterceptor.Chain)

        when:
        new GraphQLRequestInterceptor(new GraphQLQueryIdProvider(), 10000).intercept(request, chain).block()

        then:
        1 * chain.next(request) >> Mono.empty()
        def received = appender.list.find { it.formattedMessage.startsWith('GraphQL query received') }.formattedMessage
        received.contains('queryId=1 path=/graphql__FORGED client=client_FORGED___ remote=127.0.0.1_FORGED')
        appender.list.every { !(it.formattedMessage =~ /[\r\n\u0085\u2028\u2029]/) }
        appender.list.any { it.formattedMessage.startsWith('GraphQL query completed queryId=1 durationMs=') }
        servletRequest.requestURI == '/graphql\r\nFORGED'
        servletRequest.getHeader(GraphQLRequestAttributes.CLIENT_HEADER) == 'client\nFORGED\u0085\u2028\u2029'
        servletRequest.getAttribute(GraphQLRequestAttributes.QUERY_ID) == 1L
    }

    def "normal and absent log values retain their meaning"() {
        expect:
        LogSanitizer.singleLine(value) == value

        where:
        value << [null, '/graphql', 'client@example.no', '2001:db8::1']
    }

    def "interceptor captures query state and finishes it on cancellation"() {
        given:
        def servlet = new MockHttpServletRequest('POST', '/graphql')
        servlet.addHeader(HttpHeaders.AUTHORIZATION, 'Bearer captured')
        GraphQLRequestAttributes.setOrganisationId(servlet, 'org')
        GraphQLRequestAttributes.setAllowedPathPrefixes(servlet, ['/allowed'].toSet())
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servlet))
        def request = new WebGraphQlRequest(URI.create('/graphql'), new HttpHeaders(),
                new LinkedMultiValueMap(), [:], [query: '{ __typename }'], 'request', Locale.ROOT)
        def chain = Stub(WebGraphQlInterceptor.Chain) {
            next(request) >> Mono.never()
        }

        when:
        def subscription = new GraphQLRequestInterceptor(new GraphQLQueryIdProvider(), 3)
                .intercept(request, chain).subscribe()
        servlet.removeHeader(HttpHeaders.AUTHORIZATION)
        GraphQLRequestAttributes.setOrganisationId(servlet, 'changed')
        GraphQLRequestAttributes.setAllowedPathPrefixes(servlet, ['/changed'].toSet())
        def context = request.toExecutionInput().graphQLContext
        def query = context.get(GraphQLQueryContext)

        then:
        !context.hasKey(HttpServletRequest)
        query.authorization == 'Bearer captured'
        query.organisationId == 'org'
        query.allowedPathPrefixes == ['/allowed'].toSet()
        query.queryId == 1L
        !query.finished

        when:
        subscription.dispose()

        then:
        query.finished
    }

    def "a query the caller abandons is logged as cancelled"() {
        given:
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest('POST', '/graphql')))
        def request = new WebGraphQlRequest(URI.create('/graphql'), new HttpHeaders(),
                new LinkedMultiValueMap(), [:], [query: '{ __typename }'], 'request', Locale.ROOT)
        def chain = Stub(WebGraphQlInterceptor.Chain) {
            next(request) >> Mono.never()
        }

        when:
        new GraphQLRequestInterceptor(new GraphQLQueryIdProvider(), 3).intercept(request, chain).subscribe().dispose()

        then:
        outcomeLogged() == 'cancelled'
    }

    def "a failing query is logged as error"() {
        given:
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest('POST', '/graphql')))
        def request = new WebGraphQlRequest(URI.create('/graphql'), new HttpHeaders(),
                new LinkedMultiValueMap(), [:], [query: '{ __typename }'], 'request', Locale.ROOT)
        def chain = Stub(WebGraphQlInterceptor.Chain) {
            next(request) >> Mono.error(new IllegalStateException('boom'))
        }

        when:
        new GraphQLRequestInterceptor(new GraphQLQueryIdProvider(), 3).intercept(request, chain).onErrorComplete().block()

        then:
        outcomeLogged() == 'error'
    }

    private String outcomeLogged() {
        appender.list.find { it.formattedMessage.startsWith('GraphQL query completed') }
                .keyValuePairs.find { it.key == 'outcome' }.value
    }
}
