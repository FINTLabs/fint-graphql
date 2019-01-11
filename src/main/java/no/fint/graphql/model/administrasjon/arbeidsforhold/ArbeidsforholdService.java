// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforhold;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.ResourceUrlBuilder;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonArbeidsforholdService")
public class ArbeidsforholdService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ArbeidsforholdResources getArbeidsforholdResources(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        return webClientRequest.get(
                ResourceUrlBuilder.urlWithQueryParams(
                    endpoints.getAdministrasjonPersonal() + "/arbeidsforhold",
                    sinceTimeStamp),
                ArbeidsforholdResources.class,
                dfe);
    }

    public ArbeidsforholdResource getArbeidsforholdResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ArbeidsforholdResource.class, dfe);
    }
}

