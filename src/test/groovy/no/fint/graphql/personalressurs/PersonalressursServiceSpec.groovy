package no.fint.graphql.personalressurs

import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class PersonalressursServiceSpec extends Specification {

    def "Get all personalressurs"() {
        given:
        def service = new PersonalressursService(webClient: WebClient.create("https://play-with-fint.felleskomponent.no"))

        when:
        def personalressurs = service.getPersonalressurs()

        then:
        personalressurs
    }

}
