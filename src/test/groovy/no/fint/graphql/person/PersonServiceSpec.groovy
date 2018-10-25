package no.fint.graphql.person

import no.fint.graphql.model.Endpoints
import no.fint.graphql.model.felles.person.PersonService
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class PersonServiceSpec extends Specification {

    def "Get all person"() {
        given:
        def endpoint = Mock(Endpoints) {
            getFelles() >> "/administrasjon/personal"
        }
        def service = new PersonService(endpoints: endpoint, webClient: WebClient.create("https://play-with-fint.felleskomponent.no"))

        when:
        def person = service.getPersonResources()

        then:
        person
    }

}
