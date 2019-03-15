// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.arbeidsforhold;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonArbeidsforholdService")
public class ArbeidsforholdService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ArbeidsforholdResource getArbeidsforholdResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getArbeidsforholdResource(
            endpoints.getAdministrasjonPersonal() 
                + "/arbeidsforhold/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public ArbeidsforholdResource getArbeidsforholdResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ArbeidsforholdResource.class, dfe);
    }
}

