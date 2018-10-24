package no.fint.graphql.organisasjonselement

import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class OrganisasjonselementServiceSpec extends Specification {

    def "Get all organisasjonselement"() {
        given:
        def service = new OrganisasjonselementService(webClient: WebClient.create("https://play-with-fint.felleskomponent.no"))

        when:
        def organisasjonslement = service.getOrganisasjonselementResources()

        then:
        organisasjonslement
    }
}
