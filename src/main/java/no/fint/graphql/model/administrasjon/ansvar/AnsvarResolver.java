// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.ansvar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.administrasjon.fullmakt.FullmaktService;


import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;


import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.fullmakt.FullmaktResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonAnsvarResolver")
public class AnsvarResolver implements GraphQLResolver<AnsvarResource> {


    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private FullmaktService fullmaktService;


    public AnsvarResource getOverordnet(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return ansvarService.getAnsvarResource(
            Links.get(ansvar.getOverordnet()),
            dfe);
    }

    public AnsvarResource getUnderordnet(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return ansvarService.getAnsvarResource(
            Links.get(ansvar.getUnderordnet()),
            dfe);
    }

    public OrganisasjonselementResource getOrganisasjonselement(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return organisasjonselementService.getOrganisasjonselementResource(
            Links.get(ansvar.getOrganisasjonselement()),
            dfe);
    }

    public FullmaktResource getFullmakt(AnsvarResource ansvar, DataFetchingEnvironment dfe) {
        return fullmaktService.getFullmaktResource(
            Links.get(ansvar.getFullmakt()),
            dfe);
    }

}

