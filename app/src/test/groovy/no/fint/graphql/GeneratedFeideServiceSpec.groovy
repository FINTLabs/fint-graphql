package no.fint.graphql

import graphql.schema.DataFetchingEnvironment
import no.fint.graphql.model.model.elev.ElevService
import no.fint.graphql.model.model.skoleressurs.SkoleressursService
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.resource.utdanning.elev.ElevResource
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource
import org.springframework.test.util.ReflectionTestUtils
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration

class GeneratedFeideServiceSpec extends Specification {
    def "generated #name service preserves valid Feide names and removes blank identifiers"() {
        given:
        def request = Mock(WebClientRequest)
        def environment = Stub(DataFetchingEnvironment)
        def service = serviceType.getDeclaredConstructor().newInstance()
        ReflectionTestUtils.setField(service, 'webClientRequest', request)
        def resource = resourceType.getDeclaredConstructor().newInstance()
        request.get('/resource', resourceType, environment) >> Mono.just(resource)
        def identifiers = [null, new Identifikator(), new Identifikator(identifikatorverdi: ''),
                           new Identifikator(identifikatorverdi: ' \t'), new Identifikator(identifikatorverdi: 'ada@example.org')]

        expect:
        identifiers.every { identifier ->
            resource.feidenavn = identifier
            def result = service."get${name}Resource"('/resource', environment).block(Duration.ofSeconds(2))
            result.feidenavn == (identifier?.identifikatorverdi == 'ada@example.org' ? identifier : null)
        }

        where:
        name           | serviceType         | resourceType
        'Elev'         | ElevService         | ElevResource
        'Skoleressurs' | SkoleressursService | SkoleressursResource
    }
}
