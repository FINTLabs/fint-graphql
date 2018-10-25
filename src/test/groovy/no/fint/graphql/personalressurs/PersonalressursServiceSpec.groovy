package no.fint.graphql.personalressurs

import no.fint.graphql.model.Endpoints
import no.fint.graphql.model.administrasjon.personalressurs.PersonalressursService
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class PersonalressursServiceSpec extends Specification {

    def "Get all personalressurs"() {
        given:
        def endpoint = Mock(Endpoints) {
            getAdministrasjonPersonal() >> "/administrasjon/personal"
        }
        def service = new PersonalressursService(endpoints: endpoint, webClient: WebClient.create("https://play-with-fint.felleskomponent.no"))

        when:
        def personalressurs = service.getPersonalressursResources()

        then:
        personalressurs
    }

}
