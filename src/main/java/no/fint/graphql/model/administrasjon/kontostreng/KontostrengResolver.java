// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.kontostreng;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.ansvar.AnsvarService;
import no.fint.graphql.model.administrasjon.art.ArtService;
import no.fint.graphql.model.administrasjon.funksjon.FunksjonService;
import no.fint.graphql.model.administrasjon.prosjekt.ProsjektService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.kompleksedatatyper.KontostrengResource;
import no.fint.model.resource.administrasjon.kodeverk.AnsvarResource;
import no.fint.model.resource.administrasjon.kodeverk.ArtResource;
import no.fint.model.resource.administrasjon.kodeverk.FunksjonResource;
import no.fint.model.resource.administrasjon.kodeverk.ProsjektResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


    public AnsvarResource getAnsvar(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return kontostreng.getAnsvar()
                .stream()
                .map(Link::getHref)
                .map(l -> ansvarService.getAnsvarResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public ArtResource getArt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return kontostreng.getArt()
                .stream()
                .map(Link::getHref)
                .map(l -> artService.getArtResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public FunksjonResource getFunksjon(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return kontostreng.getFunksjon()
                .stream()
                .map(Link::getHref)
                .map(l -> funksjonService.getFunksjonResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public ProsjektResource getProsjekt(KontostrengResource kontostreng, DataFetchingEnvironment dfe) {
        return kontostreng.getProsjekt()
                .stream()
                .map(Link::getHref)
                .map(l -> prosjektService.getProsjektResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

