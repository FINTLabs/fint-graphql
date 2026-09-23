package no.fint.graphql

import graphql.schema.PropertyDataFetcher
import jakarta.servlet.Filter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.graphql.execution.GraphQlSource
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.filter.OncePerRequestFilter
import spock.lang.Specification
import tools.jackson.databind.ObjectMapper
import no.novari.fint.model.resource.felles.PersonResource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, TestJwtDecoderConfig, MigrationConfig], properties = [
        'spring.graphql.schema.locations=classpath:schema/**/,classpath:test-schema/',
        'server.servlet.context-path=/graphql',
        'management.server.port=0',
        'fint.graphql.blacklist='
])
@AutoConfigureWebTestClient
class GraphQLMigrationSpec extends Specification {
    @Autowired WebTestClient client
    @Autowired GraphQlSource source
    @Autowired ApplicationContext context
    @Autowired ObjectMapper mapper
    @Value('${local.management.port}') int managementPort

    def "all public queries and explicitly mapped relationships have executable bindings"() {
        given:
        def schema = source.schema()
        def registry = schema.codeRegistry
        def controllers = context.getBeansWithAnnotation(Controller)
        def mappings = controllers.values().collectMany { bean ->
            bean.class.methods.findAll { it.getAnnotation(SchemaMapping) != null || it.getAnnotation(QueryMapping) != null }
        }

        expect:
        schema.queryType.fieldDefinitions.every { field ->
            !(registry.getDataFetcher(schema.queryType, field) instanceof PropertyDataFetcher)
        }
        mappings.every { method ->
            def query = method.getAnnotation(QueryMapping)
            def mapping = method.getAnnotation(SchemaMapping)
            def typeName = query ? 'Query' : mapping.typeName()
            def fieldName = query ? (query.name() ?: method.name) : mapping.field()
            def type = schema.getObjectType(typeName)
            def field = type?.getFieldDefinition(fieldName)
            field != null && !(registry.getDataFetcher(type, field) instanceof PropertyDataFetcher)
        }
        schema.getObjectType('AvlagtProve') != null
        schema.getObjectType('OtUngdom') != null
    }

    def "servlet requests and blocking GraphQL controllers execute on virtual threads"() {
        expect:
        client.post().uri('/graphql')
                .header('Authorization', TestJwtTokens.bearerWithRoles('FINT_Client_AdministrasjonFullmakt'))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue([query: '{ virtualThread }'])
                .exchange().expectStatus().isOk()
                .expectHeader().valueEquals('X-Test-Virtual-Thread', 'true')
                .expectHeader().valueEquals('X-Test-Request-Uri', '/graphql/graphql')
                .expectBody().jsonPath('$.data.virtualThread').isEqualTo(true)
    }

    def "Jackson 3 decodes FINT values and links without Jackson 2 databind"() {
        when:
        def person = mapper.readValue('''{
          "navn":{"fornavn":"Ada","etternavn":"Lovelace"},
          "fodselsdato":"1815-12-10T00:00:00Z",
          "_links":{"elev":[{"href":"/utdanning/elev/elev/systemid/1"}]}
        }''', PersonResource)

        then:
        person.navn.fornavn == 'Ada'
        person.fodselsdato.toInstant().toString() == '1815-12-10T00:00:00Z'
        person.elev*.href == ['/utdanning/elev/elev/systemid/1']
    }

    def "Jackson 2 databind is absent from the application classpath"() {
        when:
        Class.forName('com.fasterxml.jackson.databind.ObjectMapper')

        then:
        thrown(ClassNotFoundException)
    }

    def "schema export and management probes remain public with a context path"() {
        expect:
        client.get().uri('/schema.json').exchange().expectStatus().isOk()
                .expectBody().jsonPath('$.data.__schema.queryType.name').isEqualTo('Query')
        WebTestClient.bindToServer().baseUrl("http://localhost:${managementPort}").build()
                .get().uri('/actuator/health/readiness').exchange().expectStatus().isOk()
                .expectBody().jsonPath('$.status').isEqualTo('UP')
    }

    @TestConfiguration
    static class MigrationConfig {
        @Bean VirtualThreadController virtualThreadController() { new VirtualThreadController() }
        @Bean Filter virtualThreadProbe() {
            new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                                jakarta.servlet.http.HttpServletResponse response,
                                                jakarta.servlet.FilterChain chain) {
                    response.setHeader('X-Test-Virtual-Thread', Thread.currentThread().isVirtual().toString())
                    response.setHeader('X-Test-Request-Uri', request.requestURI)
                    chain.doFilter(request, response)
                }
            }
        }
    }

    @TestComponent
    @Controller
    static class VirtualThreadController {
        @QueryMapping Boolean virtualThread() { Thread.currentThread().isVirtual() }
    }
}
