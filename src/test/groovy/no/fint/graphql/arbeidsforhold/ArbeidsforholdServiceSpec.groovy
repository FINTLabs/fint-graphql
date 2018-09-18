package no.fint.graphql.arbeidsforhold


import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class ArbeidsforholdServiceSpec extends Specification {

    def "Get all arbeidsforhold"() {
        given:
        def service = new ArbeidsforholdService(webClient: WebClient.create("https://play-with-fint.felleskomponent.no"))

        when:
        def arbeidsforhold = service.getArbeidsforhold()

        then:
        arbeidsforhold
    }
}
