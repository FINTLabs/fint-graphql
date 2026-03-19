package no.fint.graphql

import graphql.ExceptionWhileDataFetching
import graphql.GraphQLContext
import graphql.execution.ExecutionId
import graphql.execution.ResultPath
import graphql.language.SourceLocation
import graphql.schema.DataFetchingEnvironment
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import spock.lang.Specification
import no.fint.graphql.config.ConnectionProviderSettings

import jakarta.servlet.http.HttpServletRequest
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.TimeUnit

class WebClientRequestSpec extends Specification {

    private MockWebServer server = new MockWebServer()
    private String url = server.url('/').toString()
    private WebClient webClient = WebClient.create(url)
    private BlacklistService blacklistService = Mock(BlacklistService)
    private ConnectionProviderSettings connectionProviderSettings = new ConnectionProviderSettings(maxConnections: 100)
    private GraphQLQueryIdProvider queryIdProvider = new GraphQLQueryIdProvider()
    private WebClientRequest webClientRequest = new WebClientRequest(
            webClient,
            connectionProviderSettings,
            'maximumSize=1,expireAfterWrite=1s'
            ,
            queryIdProvider)

    def "Get request with token"() {
        given:
        def dfe = createDataFetchingEnvironmentMock('Bearer abc123')
        server.enqueue(new MockResponse().setResponseCode(200).setBody("response"))

        when:
        def response = webClientRequest.get(url, String, dfe).block()
        def request = server.takeRequest()

        then:
        response == 'response'
        request.getHeader(HttpHeaders.AUTHORIZATION) == 'Bearer abc123'
    }

    def "Get request without token"() {
        given:
        def dfe = createDataFetchingEnvironmentMock()

        when:
        webClientRequest.get(url, String, dfe).block()

        then:
        thrown(MissingAuthorizationException)
        server.takeRequest(200, TimeUnit.MILLISECONDS) == null
    }

    def "Get request returns status #status as WebClientResponseException"() {
        given:
        def dfe = createDataFetchingEnvironmentMock('Bearer abc123')
        server.enqueue(new MockResponse().setResponseCode(status).setBody("error"))
        def handler = new WebClientGraphQLErrorHandler()
        def path = ["query", "resource"]

        when:
        webClientRequest.get(url, String, dfe).block()

        then:
        def ex = thrown(WebClientResponseException)
        ex.rawStatusCode == status
        def error = new ExceptionWhileDataFetching(ResultPath.fromList(path), ex, new SourceLocation(1, 1))
        def mapped = handler.processErrors([error])
        mapped.size() == 1
        mapped[0].path == path
        mapped[0].message == expectedMessage(status, url)

        where:
        status << [401, 403, 404]
    }

    def "Limiter allows only one concurrent request when max-concurrent is 1"() {
        given:
        def limitedSettings = new ConnectionProviderSettings(maxConnections: 1, acquireTimeout: 100)
        def limitedRequest = new WebClientRequest(
                webClient,
                limitedSettings,
                'maximumSize=1,expireAfterWrite=1s'
                ,
                queryIdProvider)
        def dfe = createDataFetchingEnvironmentMock('Bearer abc123', 'query-1')
        def firstUrl = server.url('/one').toString()
        def secondUrl = server.url('/two').toString()
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("one")
                .setBodyDelay(500, TimeUnit.MILLISECONDS))
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("two"))

        when:
        def future1 = limitedRequest.get(firstUrl, String, dfe).toFuture()
        def future2 = limitedRequest.get(secondUrl, String, dfe).toFuture()

        then:
        def firstRequest = server.takeRequest(1, TimeUnit.SECONDS)
        firstRequest != null
        def secondRequest = server.takeRequest(200, TimeUnit.MILLISECONDS)
        secondRequest == null

        when:
        def firstResponse = future1.get(2, TimeUnit.SECONDS)
        def secondRequestAfter = server.takeRequest(2, TimeUnit.SECONDS)
        def secondResponse = future2.get(2, TimeUnit.SECONDS)

        then:
        secondRequestAfter != null
        firstResponse == "one"
        secondResponse == "two"
    }

    def "Concurrent requests are capped by max connections before entering Netty"() {
        given:
        def constrainedSettings = new ConnectionProviderSettings(
                maxConnections: 2,
                acquireMaxCount: 0
        )
        def alignedSettings = new ConnectionProviderSettings(
                maxConnections: 2,
                acquireMaxCount: 100
        )

        when:
        def constrainedRequest = new WebClientRequest(
                webClient,
                constrainedSettings,
                'maximumSize=1,expireAfterWrite=1s',
                queryIdProvider
        )

        then:
        constrainedRequest.getMaxConcurrentRequests() == 2
        alignedSettings.getMaxConnections() == 2
        constrainedSettings.getEffectiveAcquireMaxCount() == 1
    }

    def "Request scoped cache deduplicates identical resource fetches"() {
        given:
        def dfe = createDataFetchingEnvironmentMock('Bearer abc123', 'query-1')
        server.enqueue(new MockResponse().setResponseCode(200).setBody("response"))

        when:
        def response = reactor.core.publisher.Mono.zip(
                webClientRequest.get(url, String, dfe),
                webClientRequest.get(url, String, dfe)
        ).block()
        def request = server.takeRequest(1, TimeUnit.SECONDS)

        then:
        response.t1 == 'response'
        response.t2 == 'response'
        request != null
        server.takeRequest(200, TimeUnit.MILLISECONDS) == null
    }

    def "Unique outbound requests keep the originating query request counter"() {
        given:
        def servletRequest = createServletRequest('Bearer abc123')
        servletRequest.setAttribute(GraphQLRequestAttributes.QUERY_ID, 42L)
        servletRequest.setAttribute(GraphQLRequestAttributes.REQUEST_COUNTER, new AtomicLong())
        def dfe = createDataFetchingEnvironmentMock(servletRequest, 'query-1')
        def firstUrl = server.url('/first').toString()
        def secondUrl = server.url('/second').toString()
        server.enqueue(new MockResponse().setResponseCode(200).setBody("one"))
        server.enqueue(new MockResponse().setResponseCode(200).setBody("two"))

        when:
        def response = reactor.core.publisher.Mono.zip(
                webClientRequest.get(firstUrl, String, dfe),
                webClientRequest.get(secondUrl, String, dfe)
        ).block()

        then:
        [response.t1, response.t2].toSet() == ['one', 'two'] as Set
        ((AtomicLong) servletRequest.getAttribute(GraphQLRequestAttributes.REQUEST_COUNTER)).get() == 2
    }

    private static String expectedMessage(int status, String requestUrl) {
        def resourcePath = new URI(requestUrl).path
        if (status == 401) {
            return "Access unauthorized for ${resourcePath}"
        }
        if (status == 403) {
            return "Access forbidden for ${resourcePath}"
        }
        if (status == 404) {
            return "Resource not found at ${resourcePath}"
        }
        throw IllegalArgumentException("Message for status code " + status + " is not defined yet")
    }

    private DataFetchingEnvironment createDataFetchingEnvironmentMock(String token = null, String executionId = null) {
        return createDataFetchingEnvironmentMock(createServletRequest(token), executionId)
    }

    private DataFetchingEnvironment createDataFetchingEnvironmentMock(MockHttpServletRequest request, String executionId = null) {
        Mock(DataFetchingEnvironment) {
            getGraphQlContext() >> GraphQLContext.of([(HttpServletRequest.class): request])
            if (executionId != null) {
                getExecutionId() >> ExecutionId.from(executionId)
            }
        }
    }

    private MockHttpServletRequest createServletRequest(String token = null) {
        def request = new MockHttpServletRequest()
        if (token != null) {
            request.addHeader(HttpHeaders.AUTHORIZATION, token)
        }
        request
    }
}
