package no.fint.graphql.integration.personalressurs


import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class PersonalressursServiceSpec extends Specification {

    def "Get all personalressurser"() {
        given:
        def personalressursService = new PersonalressursService(webClient: WebClient.create("https://play-with-fint.felleskomponent.no"))

        when:
        def personalressurs = personalressursService.getPersonalressurs()

        then:
        personalressurs
    }

}
