// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.medlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.vurdering.VurderingService;


import no.fint.model.resource.utdanning.elev.MedlemskapResource;


import no.fint.model.resource.utdanning.vurdering.VurderingResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningMedlemskapResolver")
public class MedlemskapResolver implements GraphQLResolver<MedlemskapResource> {


    @Autowired
    private VurderingService vurderingService;


    public VurderingResource getFortlopendeVurdering(MedlemskapResource medlemskap) {
        return vurderingService.getVurderingResource(Links.get(medlemskap.getFortlopendeVurdering()));
    }

    public VurderingResource getEndeligVurdering(MedlemskapResource medlemskap) {
        return vurderingService.getVurderingResource(Links.get(medlemskap.getEndeligVurdering()));
    }

}

