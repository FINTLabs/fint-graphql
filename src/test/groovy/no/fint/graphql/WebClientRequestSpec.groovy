package no.fint.graphql

import graphql.execution.ExecutionContext
import graphql.schema.DataFetchingEnvironment
import graphql.servlet.GraphQLContext
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class WebClientRequestSpec extends Specification {

    private MockWebServer server = new MockWebServer()
    private String url = server.url('/').toString()
    private WebClient webClient = WebClient.create(url)
    private WebClientRequest webClientRequest = new WebClientRequest(webClient: webClient)

    def "Get request with token"() {
        given:
        def dfe = createDataFetchingEnvironmentMock('Bearer abc123')
        server.enqueue(new MockResponse().setResponseCode(200).setBody("response"))

        when:
        def response = webClientRequest.get(url, String, dfe)
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
        def response = webClientRequest.get(url, String, dfe)
        def request = server.takeRequest()

        then:
        response == 'response'
        !request.getHeader(HttpHeaders.AUTHORIZATION)
    }

    private DataFetchingEnvironment createDataFetchingEnvironmentMock(String token = null) {
        Mock(DataFetchingEnvironment) {
            getExecutionContext() >> Mock(ExecutionContext) {
                getContext() >> Mock(GraphQLContext) {
                    getHttpServletRequest() >> Optional.of(Mock(HttpServletRequest) {
                        if (token != null) getHeader(HttpHeaders.AUTHORIZATION) >> token
                    })
                }
            }
        }
    }
}
