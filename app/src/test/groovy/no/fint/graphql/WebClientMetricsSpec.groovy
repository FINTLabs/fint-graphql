package no.fint.graphql

import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.micrometer.observation.ObservationRegistry
import jakarta.servlet.http.HttpServletRequest
import no.fint.graphql.config.ConnectionProviderSettings
import no.fint.graphql.model.ResourceRoutes
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatusCode
import org.springframework.http.HttpMethod
import org.springframework.mock.env.MockEnvironment
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification

class WebClientMetricsSpec extends Specification {
    def meters = new SimpleMeterRegistry()
    def observations = ObservationRegistry.create()
    def catalog = new ResourceRoutes(new MockEnvironment())
    def requests = []

    def setup() {
        meters.config().meterFilter(MeterFilter.maximumAllowableTags(
                'http.client.requests', 'uri', metricBudget(), MeterFilter.deny()))
        observations.observationConfig().observationHandler(new DefaultMeterObservationHandler(meters))
    }

    def cleanup() {
        meters.close()
    }

    def "hundreds of identifiers and query values share labels without sharing cached responses"() {
        given:
        def client = requestClient { request ->
            Mono.just(ClientResponse.create(HttpStatusCode.valueOf(200)).body(request.url().rawPath).build())
        }
        def dfe = environment()
        def template = '/administrasjon/fullmakt/rolle/navn/{value}'

        when:
        def results = (1..350).collect { id ->
            client.get(template, [value: "name ${id}/%2F".toString()], String, dfe).block()
        }
        (1..350).each { id ->
            client.get("https://external.example/administrasjon/fullmakt/rolle/navn/A%2FB?q=${id}", String, dfe).block()
        }
        def beforeCacheHit = requests.size()
        def cached = client.get(template, [value: 'name 1/%2F'], String, dfe).block()

        then:
        results.toSet().size() == 350
        results.first().endsWith('/name%201%2F%252F')
        cached == results.first()
        requests.size() == beforeCacheHit
        requests.size() == 700
        requests.every { it.url().host == 'downstream.example' }
        requests.last().url().rawQuery == 'q=350'
        timers()*.id*.getTag('uri').toSet() == [template].toSet()
        timers().sum { it.count() } == 700
    }

    def "every generated route and fallback remains observable within the configured budget"() {
        given:
        def client = requestClient { Mono.just(ClientResponse.create(HttpStatusCode.valueOf(200)).body('ok').build()) }
        def dfe = environment()

        when:
        catalog.templates().each { template ->
            client.get(template.replace('{value}', 'A%2FB'), String, dfe).block()
        }
        (1..150).each { client.get("/unexpected/${it}?secret=${it}", String, dfe).block() }

        then:
        // Spring may also use none while an observation starts; reserve root and unknown too.
        catalog.templates().size() + 3 <= metricBudget()
        timers()*.id*.getTag('uri').toSet() == catalog.templates() + [UriRouteCatalog.UNKNOWN]
        timers().sum { it.count() } == catalog.templates().size() + 150
        catalog.templates().contains('/administrasjon/personal/person/fodselsnummer/{value}')
        catalog.templates().contains('/utdanning/elev/person/fodselsnummer/{value}')
    }

    def "HTTP and connection failures retain bounded URI labels"() {
        given:
        def client = requestClient { request ->
            def value = request.url().path.tokenize('/').last()
            value == 'connection' ? Mono.error(new org.springframework.web.reactive.function.client.WebClientRequestException(
                    new IOException('connection failed'), HttpMethod.GET, request.url(), request.headers())) :
                    Mono.just(ClientResponse.create(HttpStatusCode.valueOf(Integer.parseInt(value))).body('failure').build())
        }

        when:
        ['404', '500', 'connection'].each { value ->
            client.get('/administrasjon/fullmakt/rolle/navn/{value}', [value: value], String, environment())
                    .onErrorResume { Mono.just('handled') }.block()
        }

        then:
        timers().sum { it.count() } == 3
        timers()*.id*.getTag('uri').toSet() == ['/administrasjon/fullmakt/rolle/navn/{value}'].toSet()
        timers()*.id*.getTag('status').containsAll(['404', '500'])
    }

    def "configured prefixes affect matching but never metric labels"() {
        given:
        def routes = new ResourceRoutes(new MockEnvironment()
                .withProperty('fint.endpoint.administrasjon.fullmakt', 'https://configured.example/custom/prefix')
                .withProperty('fint.endpoint.utdanning.elev', 'custom/elev'))

        expect:
        routes.template(URI.create('/custom/prefix/rolle/navn/A%2FB?q=secret')) == '/administrasjon/fullmakt/rolle/navn/{value}'
        routes.template(URI.create('/custom/elev/person/fodselsnummer/123')) == '/utdanning/elev/person/fodselsnummer/{value}'
        routes.template(URI.create('/custom/prefix/rolle')) == '/administrasjon/fullmakt/rolle'
        routes.template(URI.create('/custom/prefix/rolle/navn/A/B')) == UriRouteCatalog.UNKNOWN
        routes.template(URI.create('/custom/prefix/rolle/navn/')) == UriRouteCatalog.UNKNOWN
        routes.template(URI.create('/custom/prefix/rolle/arbitrary-key/123')) == UriRouteCatalog.UNKNOWN
        routes.template(URI.create('/administrasjon/fullmakt/rolle/navn/123')) == UriRouteCatalog.UNKNOWN
        routes.templates() == catalog.templates()
    }

    def "following a link never re-encodes a coincidentally matching GraphQL argument"() {
        given:
        def client = requestClient { Mono.just(ClientResponse.create(HttpStatusCode.valueOf(200)).body('ok').build()) }
        def servlet = servletRequest()
        def dfe = Stub(DataFetchingEnvironment) {
            getGraphQlContext() >> GraphQLContext.of([(HttpServletRequest): servlet])
            getArguments() >> [navn: 'A%2FB']
        }

        when:
        client.get('/administrasjon/fullmakt/rolle/navn/A%2FB', String, dfe).block()

        then:
        requests.first().url().rawPath == '/administrasjon/fullmakt/rolle/navn/A%2FB'
    }

    private WebClientRequest requestClient(Closure exchange) {
        def webClient = WebClient.builder().observationRegistry(observations).exchangeFunction { request ->
            requests.add(request)
            exchange.call(request)
        }.build()
        new WebClientRequest(webClient, new ConnectionProviderSettings(), 'http://downstream.example',
                'beta.example', 'maximumSize=100,expireAfterWrite=1m', 10000, 'V4', new GraphQLQueryIdProvider(), catalog)
    }

    private DataFetchingEnvironment environment() {
        def servlet = servletRequest()
        Stub(DataFetchingEnvironment) {
            getGraphQlContext() >> GraphQLContext.of([(HttpServletRequest): servlet])
        }
    }

    private static MockHttpServletRequest servletRequest() {
        def servlet = new MockHttpServletRequest()
        servlet.addHeader('Authorization', 'Bearer test')
        GraphQLRequestAttributes.setOrganisationId(servlet, 'org')
        GraphQLRequestAttributes.setAllowedPathPrefixes(servlet, ['/'].toSet())
        servlet
    }

    private def timers() { meters.find('http.client.requests').timers().toList() }

    private static int metricBudget() {
        def yaml = new YamlPropertiesFactoryBean()
        yaml.setResources(new ClassPathResource('application.yml'))
        Integer.parseInt(yaml.object.getProperty('management.metrics.web.client.max-uri-tags'))
    }
}
