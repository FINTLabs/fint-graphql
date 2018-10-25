package no.fint.graphql.organisasjonselement

import no.fint.graphql.model.Endpoints
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class OrganisasjonselementServiceSpec extends Specification {

    def "Get all organisasjonselement"() {
        given:
        def endpoint = Mock(Endpoints) {
            getAdministrasjonOrganisasjon() >> "/administrasjon/organisasjon"
        }
        def service = new OrganisasjonselementService(endpoints: endpoint, webClient: WebClient.create("https://play-with-fint.felleskomponent.no"))

        when:
        def organisasjonslement = service.getOrganisasjonselementResources()

        then:
        organisasjonslement
    }
}
