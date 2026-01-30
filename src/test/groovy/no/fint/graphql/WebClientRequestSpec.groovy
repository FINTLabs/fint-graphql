package no.fint.graphql

import graphql.ExceptionWhileDataFetching
import graphql.execution.ExecutionPath
import graphql.language.SourceLocation
import graphql.schema.DataFetchingEnvironment
import graphql.servlet.context.GraphQLServletContext
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class WebClientRequestSpec extends Specification {

    private MockWebServer server = new MockWebServer()
    private String url = server.url('/').toString()
    private WebClient webClient = WebClient.create(url)
    private BlacklistService blacklistService = Mock(BlacklistService)
    private WebClientRequest webClientRequest = new WebClientRequest(
            webClient,
            'maximumSize=1,expireAfterWrite=1s',
            blacklistService)

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
        server.enqueue(new MockResponse().setResponseCode(200).setBody("response"))

        when:
        def response = webClientRequest.get(url, String, dfe).block()
        def request = server.takeRequest()

        then:
        response == 'response'
        !request.getHeader(HttpHeaders.AUTHORIZATION)
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

    private static String expectedMessage(int status, String requestUrl) {
        def resourcePath = new URI(requestUrl).path
        if (status == 401) {
            return "Access unauthorized for ${resourcePath}"
        }
        if (status == 403) {
            return "Access forbidden for ${resourcePath}"
        }
        if (status == 404) {
            return "Failed to find resource ${resourcePath}"
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
