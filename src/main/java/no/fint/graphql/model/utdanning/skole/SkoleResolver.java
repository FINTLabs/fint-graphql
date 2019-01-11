// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.skole;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skoleeiertype.SkoleeiertypeService;
import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.utdanningsprogram.UtdanningsprogramService;


import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;


import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningSkoleResolver")
public class SkoleResolver implements GraphQLResolver<SkoleResource> {


    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleeiertypeService skoleeiertypeService;

    @Autowired
    private BasisgruppeService basisgruppeService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private UtdanningsprogramService utdanningsprogramService;


    public OrganisasjonselementResource getOrganisasjon(SkoleResource skole, DataFetchingEnvironment dfe) {
        return organisasjonselementService.getOrganisasjonselementResource(
            Links.get(skole.getOrganisasjon()),
            dfe);
    }

    public FagResource getFag(SkoleResource skole, DataFetchingEnvironment dfe) {
        return fagService.getFagResource(
            Links.get(skole.getFag()),
            dfe);
    }

    public SkoleeiertypeResource getSkoleeierType(SkoleResource skole, DataFetchingEnvironment dfe) {
        return skoleeiertypeService.getSkoleeiertypeResource(
            Links.get(skole.getSkoleeierType()),
            dfe);
    }

    public BasisgruppeResource getBasisgruppe(SkoleResource skole, DataFetchingEnvironment dfe) {
        return basisgruppeService.getBasisgruppeResource(
            Links.get(skole.getBasisgruppe()),
            dfe);
    }

    public ElevforholdResource getElevforhold(SkoleResource skole, DataFetchingEnvironment dfe) {
        return elevforholdService.getElevforholdResource(
            Links.get(skole.getElevforhold()),
            dfe);
    }

    public KontaktlarergruppeResource getKontaktlarergruppe(SkoleResource skole, DataFetchingEnvironment dfe) {
        return kontaktlarergruppeService.getKontaktlarergruppeResource(
            Links.get(skole.getKontaktlarergruppe()),
            dfe);
    }

    public SkoleressursResource getSkoleressurs(SkoleResource skole, DataFetchingEnvironment dfe) {
        return skoleressursService.getSkoleressursResource(
            Links.get(skole.getSkoleressurs()),
            dfe);
    }

    public UndervisningsforholdResource getUndervisningsforhold(SkoleResource skole, DataFetchingEnvironment dfe) {
        return undervisningsforholdService.getUndervisningsforholdResource(
            Links.get(skole.getUndervisningsforhold()),
            dfe);
    }

    public UndervisningsgruppeResource getUndervisningsgruppe(SkoleResource skole, DataFetchingEnvironment dfe) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(
            Links.get(skole.getUndervisningsgruppe()),
            dfe);
    }

    public EksamensgruppeResource getEksamensgruppe(SkoleResource skole, DataFetchingEnvironment dfe) {
        return eksamensgruppeService.getEksamensgruppeResource(
            Links.get(skole.getEksamensgruppe()),
            dfe);
    }

    public UtdanningsprogramResource getUtdanningsprogram(SkoleResource skole, DataFetchingEnvironment dfe) {
        return utdanningsprogramService.getUtdanningsprogramResource(
            Links.get(skole.getUtdanningsprogram()),
            dfe);
    }

}

