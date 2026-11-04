package no.fint.graphql

import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import mockwebserver3.RecordedRequest
import no.fint.graphql.config.ConnectionProviderSettings
import no.fint.graphql.config.RelationPaginationSettings
import no.fint.graphql.model.Endpoints
import no.fint.graphql.model.ResourceRoutes
import no.fint.graphql.model.model.elevforhold.ElevforholdService
import no.fint.graphql.model.model.skole.SkoleResolver
import no.fint.graphql.model.model.skoleressurs.SkoleressursService
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.utdanning.elev.ElevforholdResource
import no.novari.fint.model.resource.utdanning.utdanningsprogram.SkoleResource
import org.dataloader.DataLoaderRegistry
import org.jetbrains.annotations.NotNull
import org.springframework.graphql.execution.DefaultBatchLoaderRegistry
import org.springframework.http.HttpHeaders
import org.springframework.mock.env.MockEnvironment
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification
import tools.jackson.databind.json.JsonMapper

import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

class RelationPageFetcherSpec extends Specification {
    private static final String BASE = 'https://beta.felleskomponent.no'
    private static final String COLLECTION = '/utdanning/elev/elevforhold'

    private MockWebServer server = new MockWebServer().tap { start() }
    private List<String> requests = new CopyOnWriteArrayList<>()
    private Map<String, MockResponse> responses = new ConcurrentHashMap<>()
    private DefaultBatchLoaderRegistry batchLoaders = new DefaultBatchLoaderRegistry()
    private List<DataLoaderRegistry> dataLoaders = []

    def setup() {
        server.dispatcher = new Dispatcher() {
            @Override
            MockResponse dispatch(@NotNull RecordedRequest request) {
                requests << request.target
                responses.getOrDefault(request.target, new MockResponse.Builder().code(404).build())
            }
        }
    }

    def cleanup() {
        server.close()
    }

    def "a relation at the threshold is requested one resource at a time"() {
        given:
        stubIndividual('A', 'B')

        expect:
        ids(resolve(fetcher(2, 2), links('A', 'B'))) == ['A', 'B']
        requests.toSet() == individualTargets('A', 'B').toSet()
    }

    def "a relation above the threshold follows next links and keeps the order of the relation"() {
        given:
        stubPage("$COLLECTION?size=2", ['A', 'B'], "$BASE$COLLECTION?offset=2&size=2&cursor=c1")
        stubPage("$COLLECTION?offset=2&size=2&cursor=c1", ['C', 'D'], null)

        expect:
        ids(resolve(fetcher(2, 2), links('D', 'A', 'C'))) == ['D', 'A', 'C']
        requests == ["$COLLECTION?size=2", "$COLLECTION?offset=2&size=2&cursor=c1"]
    }

    def "the walk stops as soon as every wanted resource is found"() {
        given:
        stubPage("$COLLECTION?size=3", ['A', 'B', 'C'], "$BASE$COLLECTION?offset=3&size=3&cursor=never")

        expect:
        ids(resolve(fetcher(2, 3), links('A', 'B', 'C'))) == ['A', 'B', 'C']
        requests == ["$COLLECTION?size=3"]
    }

    def "a resource missing from every page is requested individually and a 404 keeps its null slot"() {
        given:
        stubPage("$COLLECTION?size=2", ['A', 'B'], null)
        stubIndividual('C')

        expect:
        ids(resolve(fetcher(2, 2), links('A', 'GONE', 'C', 'B'))) == ['A', null, 'C', 'B']
        requests.toSet() == ["$COLLECTION?size=2", *individualTargets('GONE', 'C')].toSet()
    }

    def "a repeating next link ends the walk"() {
        given:
        stubPage("$COLLECTION?size=2", ['A'], "$BASE$COLLECTION?cursor=loop")
        stubPage("$COLLECTION?cursor=loop", ['B'], "$BASE$COLLECTION?cursor=loop")
        stubIndividual('C')

        expect:
        ids(resolve(fetcher(2, 2), links('A', 'B', 'C'))) == ['A', 'B', 'C']
        requests == ["$COLLECTION?size=2", "$COLLECTION?cursor=loop", individualTargets('C')[0]]
    }

    def "a failing page keeps what was found and requests the rest individually"() {
        given:
        stubPage("$COLLECTION?size=2", ['A', 'B'], "$BASE$COLLECTION?cursor=boom")
        responses["$COLLECTION?cursor=boom".toString()] = new MockResponse.Builder().code(500).build()
        stubIndividual('C')

        expect:
        ids(resolve(fetcher(2, 2), links('A', 'B', 'C'))) == ['A', 'B', 'C']
        requests == ["$COLLECTION?size=2", "$COLLECTION?cursor=boom", individualTargets('C')[0]]
    }

    def "a malformed entry falls back to individual requests instead of failing the relation"() {
        given:
        responses["$COLLECTION?size=2".toString()] = json("""{"_embedded":{"_entries":[
                {"systemId":"not-an-object","_links":{"self":[{"href":"$BASE$COLLECTION/systemid/A"}]}}]}}""")
        stubIndividual('A', 'B', 'C')

        expect:
        ids(resolve(fetcher(2, 2), links('A', 'B', 'C'))) == ['A', 'B', 'C']
    }

    def "self links match on any identifier, ignoring identifier case but not value case"() {
        given:
        responses["$COLLECTION?size=10".toString()] = json("""{"_embedded":{"_entries":[
                ${entry('sys-1', ["$COLLECTION/systemid/sys-1", "$COLLECTION/elevnummer/E1"])},
                ${entry('sys-2', ["$COLLECTION/systemid/A%2FB"])},
                ${entry('sys-3', ["$COLLECTION/SYSTEMID/case"])},
                ${entry('sys-4', ["$COLLECTION/systemid/CASE"])}
                ]}}""")
        def relation = [
                new Link("$BASE$COLLECTION/elevnummer/E1"),
                new Link("$BASE$COLLECTION/systemId/A%2FB"),
                new Link("$BASE$COLLECTION/systemid/CASE"),
        ]

        expect:
        ids(resolve(fetcher(2, 10), relation)) == ['sys-1', 'sys-2', 'sys-4']
        requests == ["$COLLECTION?size=10"]
    }

    def "duplicate links to one resource are all answered from the page"() {
        given:
        stubPage("$COLLECTION?size=2", ['A', 'B'], null)
        def relation = [new Link("$BASE$COLLECTION/systemid/A"), new Link("http://other$COLLECTION/systemid/A"), *links('A', 'B')]

        expect:
        ids(resolve(fetcher(1, 2), relation)) == ['A', 'A', 'A', 'B']
        requests == ["$COLLECTION?size=2"]
    }

    def "the threshold is applied per collection"() {
        given:
        stubPage("$COLLECTION?size=2", ['A', 'B', 'C'], null)
        def relation = [*links('A', 'B', 'C'), new Link("$BASE/utdanning/annet/elevforhold/systemid/X")]

        when:
        prefetch(fetcher(2, 2), relation)

        then:
        requests == ["$COLLECTION?size=2"]
    }

    def "identical relations in one query share a walk"() {
        given:
        stubPage("$COLLECTION?size=2", ['A', 'B', 'C'], null)
        def environment = dfe()
        def fetcher = fetcher(2, 2)

        when:
        def first = resolve(fetcher, links('A', 'B', 'C'), environment)
        def second = resolve(fetcher, links('C', 'B', 'A'), environment)

        then:
        ids(first) == ['A', 'B', 'C']
        ids(second) == ['C', 'B', 'A']
        requests == ["$COLLECTION?size=2"]
    }

    def "found resources are not visible to another query"() {
        given:
        stubPage("$COLLECTION?size=2", ['A', 'B', 'C'], null)
        def fetcher = fetcher(2, 2)

        when:
        resolve(fetcher, links('A', 'B', 'C'), dfe())
        resolve(fetcher, links('A', 'B', 'C'), dfe())

        then:
        requests == ["$COLLECTION?size=2", "$COLLECTION?size=2"]
    }

    def "pages carry the credentials of the query and next links never leave the configured root"() {
        given:
        stubPage("$COLLECTION?size=2", ['A'], "https://evil.example.test$COLLECTION?cursor=c1")
        stubPage("$COLLECTION?cursor=c1", ['B', 'C'], null)

        when:
        resolve(fetcher(2, 2), links('A', 'B', 'C'))
        def first = server.takeRequest(1, TimeUnit.SECONDS)
        def second = server.takeRequest(1, TimeUnit.SECONDS)

        then:
        [first, second].every {
            it.headers.get(HttpHeaders.AUTHORIZATION) == 'Bearer abc123' && it.headers.get('x-org-id') == 'org-1'
        }
        second.target == "$COLLECTION?cursor=c1"
    }

    def "a collection outside the allowed paths is never requested"() {
        given:
        def environment = dfe(['/utdanning/vurdering/'])

        when:
        prefetch(fetcher(2, 2), links('A', 'B', 'C'), environment)

        then:
        requests.isEmpty()
    }

    def "a finished query does not start a walk"() {
        given:
        def environment = dfe()
        environment.graphQlContext.get(GraphQLQueryContext).finish()

        when:
        prefetch(fetcher(2, 2), links('A', 'B', 'C'), environment)

        then:
        requests.isEmpty()
    }

    def "resources found in pages get the same normalization as individual lookups"() {
        given:
        def collection = '/utdanning/elev/skoleressurs'
        responses["$collection?size=2".toString()] = json("""{"_embedded":{"_entries":[
                {"systemId":{"identifikatorverdi":"A"},"feidenavn":{"identifikatorverdi":" "},
                 "_links":{"self":[{"href":"$BASE$collection/systemid/A"}]}},
                {"systemId":{"identifikatorverdi":"B"},"feidenavn":{"identifikatorverdi":"b@feide"},
                 "_links":{"self":[{"href":"$BASE$collection/systemid/B"}]}},
                {"systemId":{"identifikatorverdi":"C"},"_links":{"self":[{"href":"$BASE$collection/systemid/C"}]}}
                ]}}""")
        def skole = new SkoleResource()
        ['A', 'B', 'C'].each { skole.addSkoleressurs(new Link("$BASE$collection/systemid/$it")) }

        when:
        def pending = resolver(fetcher(2, 2)).getSkoleressurs(skole, dfe()).toCompletableFuture()
        dispatch()
        def result = pending.get(5, TimeUnit.SECONDS)

        then:
        result*.systemId*.identifikatorverdi == ['A', 'B', 'C']
        result[0].feidenavn == null
        result[1].feidenavn.identifikatorverdi == 'b@feide'
        requests == ["$collection?size=2"]
    }

    def "links from several parents are counted together and share one walk"() {
        given: "each parent is below the threshold of 3, but together they are above it"
        stubPage("$COLLECTION?size=10", ['A', 'B', 'C', 'D'], null)
        def fetcher = fetcher(3, 10)
        def environment = dfe()

        when:
        def first = resolveLater(fetcher, links('A', 'B'), environment)
        def second = resolveLater(fetcher, links('C', 'D'), environment)
        dispatch()

        then:
        ids(first.get(5, TimeUnit.SECONDS)) == ['A', 'B']
        ids(second.get(5, TimeUnit.SECONDS)) == ['C', 'D']
        requests == ["$COLLECTION?size=10"]
    }

    def "the same resource wanted by many parents counts once"() {
        given:
        stubIndividual('A')
        def fetcher = fetcher(2, 10)
        def environment = dfe()

        when:
        def pending = (1..5).collect { resolveLater(fetcher, links('A'), environment) }
        dispatch()

        then:
        pending.every { ids(it.get(5, TimeUnit.SECONDS)) == ['A'] }
        requests == individualTargets('A')
    }

    def "a walk follows next links whatever total_items says"() {
        given: "2 resources are left, and total_items suggests many pages remain"
        stubPage("$COLLECTION?size=2", ['A'], "$BASE$COLLECTION?cursor=c1", 100)
        stubPage("$COLLECTION?cursor=c1", ['B', 'C'], null, 100)

        expect:
        ids(resolve(fetcher(2, 2), links('A', 'B', 'C'))) == ['A', 'B', 'C']
        requests == ["$COLLECTION?size=2", "$COLLECTION?cursor=c1"]
    }

    private static List<String> ids(List<ElevforholdResource> resources) {
        resources.collect { it?.systemId?.identifikatorverdi }
    }

    private static List<Link> links(String... ids) {
        ids.collect { new Link("$BASE$COLLECTION/systemid/$it") }
    }

    private static List<String> individualTargets(String... ids) {
        ids.collect { "$COLLECTION/systemid/$it".toString() }
    }

    private static String entry(String systemId, List<String> selfPaths = null) {
        def self = (selfPaths ?: ["$COLLECTION/systemid/$systemId"]).collect { """{"href":"$BASE$it"}""" }.join(',')
        """{"systemId":{"identifikatorverdi":"$systemId"},"_links":{"self":[$self]}}"""
    }

    private void stubPage(String target, List<String> ids, String next, Integer totalItems = null) {
        def links = next ? """"next":[{"href":"$next"}]""" : ''
        def total = totalItems == null ? '' : ",\"total_items\":$totalItems"
        responses[target.toString()] = json("""{"_embedded":{"_entries":[${ids.collect { entry(it) }.join(',')}]},"_links":{$links}$total}""")
    }

    private void stubIndividual(String... ids) {
        ids.each { responses["$COLLECTION/systemid/$it".toString()] = json(entry(it)) }
    }

    private static MockResponse json(String body) {
        new MockResponse.Builder().code(200).setHeader(HttpHeaders.CONTENT_TYPE, 'application/json').body(body).build()
    }

    private WebClientRequest createRequest(String root) {
        new WebClientRequest(WebClient.create(root), new ConnectionProviderSettings(maxConnections: 100), root,
                'beta.felleskomponent.no', 'maximumSize=1,expireAfterWrite=1s', 'V4',
                new GraphQLQueryIdProvider(), new ResourceRoutes(new MockEnvironment()))
    }

    private RelationPageFetcher fetcher(int threshold, int pageSize) {
        new RelationPageFetcher(sharedRequest(), JsonMapper.builder().build(),
                new RelationPaginationSettings(threshold: threshold, pageSize: pageSize), batchLoaders)
    }

    private WebClientRequest shared

    private WebClientRequest sharedRequest() {
        shared = shared ?: createRequest(server.url('/').toString())
    }

    private List<ElevforholdResource> resolve(RelationPageFetcher fetcher, List<Link> relation,
                                              DataFetchingEnvironment environment = dfe()) {
        def pending = resolveLater(fetcher, relation, environment)
        dispatch()
        pending.get(5, TimeUnit.SECONDS)
    }

    private CompletableFuture<List<ElevforholdResource>> resolveLater(RelationPageFetcher fetcher, List<Link> relation,
                                                                      DataFetchingEnvironment environment) {
        def skole = new SkoleResource()
        relation.each { skole.addElevforhold(it) }
        resolver(fetcher).getElevforhold(skole, environment).toCompletableFuture()
    }

    private void prefetch(RelationPageFetcher fetcher, List<Link> relation, DataFetchingEnvironment environment = dfe()) {
        def pending = fetcher.prefetch(relation, ElevforholdResource, environment)
        dispatch()
        pending.block(Duration.ofSeconds(5))
    }

    /** GraphQL Java does this once every resolver at a query level has run. */
    private void dispatch() {
        dataLoaders.each { it.dispatchAll() }
    }

    private SkoleResolver resolver(RelationPageFetcher fetcher) {
        def request = sharedRequest()
        def elevforhold = new ElevforholdService()
        ReflectionTestUtils.setField(elevforhold, 'webClientRequest', request)
        ReflectionTestUtils.setField(elevforhold, 'endpoints', new Endpoints())
        def skoleressurs = new SkoleressursService()
        ReflectionTestUtils.setField(skoleressurs, 'webClientRequest', request)
        ReflectionTestUtils.setField(skoleressurs, 'endpoints', new Endpoints())
        def resolver = new SkoleResolver()
        ReflectionTestUtils.setField(resolver, 'relationPageFetcher', fetcher)
        ReflectionTestUtils.setField(resolver, 'elevforholdService', elevforhold)
        ReflectionTestUtils.setField(resolver, 'skoleressursService', skoleressurs)
        resolver
    }

    private DataFetchingEnvironment dfe(Collection<String> allowedPrefixes = ['/utdanning/elev/']) {
        def servletRequest = new MockHttpServletRequest()
        servletRequest.addHeader(HttpHeaders.AUTHORIZATION, 'Bearer abc123')
        GraphQLRequestAttributes.setAllowedPathPrefixes(servletRequest, new LinkedHashSet<>(allowedPrefixes))
        GraphQLRequestAttributes.setOrganisationId(servletRequest, 'org-1')
        def context = GraphQLContext.of([(GraphQLQueryContext): GraphQLQueryContext.capture(servletRequest, 10000)])
        def loaders = new DataLoaderRegistry()
        dataLoaders << loaders
        Mock(DataFetchingEnvironment) {
            getGraphQlContext() >> context
            getDataLoader(_ as String) >> { String name ->
                if (loaders.getDataLoader(name) == null) {
                    batchLoaders.registerDataLoaders(loaders, context)
                }
                loaders.getDataLoader(name)
            }
        }
    }
}
