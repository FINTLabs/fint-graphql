package no.fint.graphql

import graphql.ExceptionWhileDataFetching
import graphql.execution.ExecutionPath
import graphql.execution.ExecutionId
import graphql.language.SourceLocation
import graphql.schema.DataFetchingEnvironment
import graphql.servlet.context.GraphQLServletContext
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import spock.lang.Specification
import no.fint.graphql.config.ConnectionProviderSettings
import no.fint.graphql.dataloader.ResourceDataLoader

import javax.servlet.http.HttpServletRequest
import java.time.Duration
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
            'maximumSize=1,expireAfterWrite=1s',
            blacklistService,
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
        def error = new ExceptionWhileDataFetching(ExecutionPath.fromList(path), ex, new SourceLocation(1, 1))
        def mapped = handler.processErrors([error])
        mapped.size() == 1
        mapped[0].path == path
        mapped[0].message == expectedMessage(status, url)

        where:
        status << [401, 403, 404]
    }

    def "DataLoader limits concurrent requests when max-concurrent is 1"() {
        given:
        def limitedSettings = new ConnectionProviderSettings(maxConnections: 1)
        def limitedRequest = new WebClientRequest(
                webClient,
                limitedSettings,
                'maximumSize=1,expireAfterWrite=1s',
                blacklistService,
                queryIdProvider)
        def servletContext = Mock(GraphQLServletContext) {
            getHttpServletRequest() >> Mock(HttpServletRequest) {
                getHeader(HttpHeaders.AUTHORIZATION) >> 'Bearer abc123'
            }
        }
        def dataLoader = ResourceDataLoader.newDataLoader(limitedRequest, servletContext)
        def dfe = Mock(DataFetchingEnvironment) {
            getDataLoader(ResourceDataLoader.NAME) >> dataLoader
            getContext() >> servletContext
            getExecutionId() >> ExecutionId.generate()
        }
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("one")
                .setBodyDelay(500, TimeUnit.MILLISECONDS))
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("two"))

        when:
        def future1 = limitedRequest.get(url + "one", String, dfe).toFuture()
        def future2 = limitedRequest.get(url + "two", String, dfe).toFuture()
        dataLoader.dispatch()

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

    def "Request-scoped DataLoader dispatches queued load without instrumentation"() {
        given:
        def servletContext = Mock(GraphQLServletContext) {
            getHttpServletRequest() >> Mock(HttpServletRequest) {
                getHeader(HttpHeaders.AUTHORIZATION) >> 'Bearer abc123'
            }
        }
        def dfe = Mock(DataFetchingEnvironment) {
            getContext() >> servletContext
            getExecutionId() >> ExecutionId.generate()
        }
        server.enqueue(new MockResponse().setResponseCode(200).setBody("response"))

        when:
        def response = webClientRequest.get(url, String, dfe).block(Duration.ofSeconds(1))
        def request = server.takeRequest(1, TimeUnit.SECONDS)

        then:
        response == 'response'
        request != null
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

    private DataFetchingEnvironment createDataFetchingEnvironmentMock(String token = null) {
        Mock(DataFetchingEnvironment) {
            getContext() >> Mock(GraphQLServletContext) {
                getHttpServletRequest() >> Mock(HttpServletRequest) {
                    if (token != null) getHeader(HttpHeaders.AUTHORIZATION) >> token
                }
            }
        }
    }
}
