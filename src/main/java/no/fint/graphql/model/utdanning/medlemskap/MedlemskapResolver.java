// Built from tag release-3.2

package no.fint.graphql.model.utdanning.medlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.vurdering.VurderingService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;
import no.fint.model.resource.utdanning.vurdering.VurderingResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningMedlemskapResolver")
public class MedlemskapResolver implements GraphQLResolver<MedlemskapResource> {

    @Autowired
    private VurderingService vurderingService;


    public List<VurderingResource> getFortlopendeVurdering(MedlemskapResource medlemskap, DataFetchingEnvironment dfe) {
        return medlemskap.getFortlopendeVurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> vurderingService.getVurderingResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public VurderingResource getEndeligVurdering(MedlemskapResource medlemskap, DataFetchingEnvironment dfe) {
        return medlemskap.getEndeligVurdering()
                .stream()
                .map(Link::getHref)
                .map(l -> vurderingService.getVurderingResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

