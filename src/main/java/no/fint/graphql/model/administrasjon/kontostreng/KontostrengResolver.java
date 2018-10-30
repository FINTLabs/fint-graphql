// Built from tag master

package no.fint.graphql.model.administrasjon.kontostreng;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.art.ArtService;
import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.prosjekt.ProsjektService;


import no.fint.model.resource.administrasjon.kompleksedatatyper.KontostrengResource;


import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("administrasjonKontostrengResolver")
public class KontostrengResolver implements GraphQLResolver<KontostrengResource> {


    @Autowired
    private AnsvarService ansvarService;

    @Autowired
    private ArtService artService;

    @Autowired
    private FunksjonService funksjonService;

    @Autowired
    private ProsjektService prosjektService;


    public AnsvarResource getAnsvar(KontostrengResource kontostreng) {
        return ansvarService.getAnsvarResource(Links.get(kontostreng.getAnsvar()));
    }

    public ArtResource getArt(KontostrengResource kontostreng) {
        return artService.getArtResource(Links.get(kontostreng.getArt()));
    }

    public FunksjonResource getFunksjon(KontostrengResource kontostreng) {
        return funksjonService.getFunksjonResource(Links.get(kontostreng.getFunksjon()));
    }

    public ProsjektResource getProsjekt(KontostrengResource kontostreng) {
        return prosjektService.getProsjektResource(Links.get(kontostreng.getProsjekt()));
    }

}

