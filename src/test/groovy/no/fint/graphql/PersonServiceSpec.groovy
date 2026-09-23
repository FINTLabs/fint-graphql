package no.fint.graphql

import graphql.schema.DataFetchingEnvironment
import no.fint.graphql.model.Endpoints
import no.fint.graphql.model.model.person.PersonService
import no.novari.fint.model.felles.kompleksedatatyper.Identifikator
import no.novari.fint.model.felles.kompleksedatatyper.Personnavn
import no.novari.fint.model.resource.Link
import no.novari.fint.model.resource.felles.PersonResource
import org.springframework.test.util.ReflectionTestUtils
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration
import java.nio.file.Files
import java.nio.file.Path

class PersonServiceSpec extends Specification {
    def webClientRequest = Mock(WebClientRequest)
    def environment = Stub(DataFetchingEnvironment)
    def service = new PersonService()

    def setup() {
        def endpoints = new Endpoints()
        ReflectionTestUtils.setField(endpoints, 'administrasjonPersonal', '/administrasjon/personal')
        ReflectionTestUtils.setField(endpoints, 'utdanningElev', '/utdanning/elev')
        ReflectionTestUtils.setField(service, 'webClientRequest', webClientRequest)
        ReflectionTestUtils.setField(service, 'endpoints', endpoints)
    }

    def "merge keeps administrative values fills missing fields and unions links without mutating cached inputs"() {
        given:
        def admin = new PersonResource(
                fodselsnummer: new Identifikator(identifikatorverdi: '12345678910'),
                navn: new Personnavn(fornavn: 'Ada', etternavn: 'Byron'))
        admin.addPersonalressurs(new Link('/employee/1'))
        admin.addForeldre(new Link('/parent/shared'))
        admin.addForeldre(new Link('/parent/admin'))
        def student = new PersonResource(
                navn: new Personnavn(fornavn: 'Grace', etternavn: 'Hopper'),
                bilde: 'student-image')
        student.addElev(new Link('/student/1'))
        student.addForeldre(new Link('/parent/shared'))
        student.addForeldre(new Link('/parent/student'))
        webClientRequest.get('/administrasjon/personal/person/fodselsnummer/12345678910', PersonResource, environment) >> Mono.just(admin)
        webClientRequest.get('/utdanning/elev/person/fodselsnummer/12345678910', PersonResource, environment) >> Mono.just(student)

        when:
        def merged = service.getPersonResourceById('fodselsnummer', '12345678910', environment).block(Duration.ofSeconds(2))

        then:
        merged.navn.fornavn == 'Ada'
        merged.navn.etternavn == 'Byron'
        merged.bilde == 'student-image'
        merged.fodselsnummer.identifikatorverdi == '12345678910'
        merged.personalressurs*.href == ['/employee/1']
        merged.elev*.href == ['/student/1']
        merged.foreldre*.href == ['/parent/shared', '/parent/student', '/parent/admin']
        !merged.is(admin)
        !merged.is(student)
        admin.bilde == null
        admin.elev.empty
        admin.foreldre*.href == ['/parent/shared', '/parent/admin']
        student.personalressurs.empty
        student.foreldre*.href == ['/parent/shared', '/parent/student']

        when: 'another alias reads the same request-cached resources'
        def again = service.getPersonResourceById('fodselsnummer', '12345678910', environment).block(Duration.ofSeconds(2))
        merged.addElev(new Link('/student/extra'))

        then:
        again.elev*.href == ['/student/1']
        student.elev*.href == ['/student/1']
        again.foreldre*.href == merged.foreldre*.href
    }

    def "an immediate failure for #failingPath does not discard the other permitted lookup"() {
        given:
        def permitted = new PersonResource(bilde: 'permitted')
        webClientRequest.get("${failingPath}/person/fodselsnummer/123", PersonResource, environment) >> {
            throw new UnauthorizedResourceAccessException('Forbidden', failingPath, '1', '1')
        }
        webClientRequest.get("${permittedPath}/person/fodselsnummer/123", PersonResource, environment) >> Mono.just(permitted)

        expect:
        service.getPersonResourceById('fodselsnummer', '123', environment).block(Duration.ofSeconds(2)).bilde == 'permitted'

        where:
        failingPath                | permittedPath
        '/administrasjon/personal' | '/utdanning/elev'
        '/utdanning/elev'          | '/administrasjon/personal'
    }

    def "generation template matches the tested custom service"() {
        expect:
        Files.readString(Path.of('PersonService.txt')) ==
                Files.readString(Path.of('src/main/java/no/fint/graphql/model/model/person/PersonService.java'))
    }
}
