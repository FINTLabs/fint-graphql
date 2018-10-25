package no.fint.graphql.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Endpoints {

    @Value("${fint.endpoint.felles:/felles}")
    private String felles;

    @Value("${fint.endpoint.felles.kodeverk:/felles/kodeverk}")
    private String fellesKodeverk;

    @Value("${fint.endpoint.felles.kodeverk.iso:/felles/kodeverk/iso}")
    private String fellesKodeverkIso;

    @Value("${fint.endpoint.administrasjon.fullmakt:/administrasjon/fullmakt}")
    private String administrasjonFullmakt;

    @Value("${fint.endpoint.administrasjon.kodeverk:/administrasjon/kodeverk}")
    private String administrasjonKodeverk;

    @Value("${fint.endpoint.administrasjon.organisasjon:/administrasjon/organisasjon}")
    private String administrasjonOrganisasjon;

    @Value("${fint.endpoint.administrasjon.personal:/administrasjon/personal}")
    private String administrasjonPersonal;

    @Value("${fint.endpoint.utdanning.elev:/utdanning/elev}")
    private String utdanningElev;

    @Value("${fint.endpoint.utdanning.timeplan:/utdanning/timeplan}")
    private String utdanningTimeplan;

    @Value("${fint.endpoint.utdanning.utdanningsprogram:/utdanning/utdanningsprogram}")
    private String utdanningUtdanningsprogram;

    @Value("${fint.endpoint.utdanning.vurdering:/utdanning/vurdering}")
    private String utdanningVurdering;

    @Value("${fint.endpoint.utdanning.kodeverk:/utdanning/kodeverk}")
    private String utdanningKodeverk;

}
