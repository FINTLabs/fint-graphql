// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforholdstype;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.WebClientRequest;
import no.fint.graphql.model.Endpoints;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("administrasjonArbeidsforholdstypeService")
public class ArbeidsforholdstypeService {

    @Autowired
    private WebClientRequest webClientRequest;

    @Autowired
    private Endpoints endpoints;

    public ArbeidsforholdstypeResource getArbeidsforholdstypeResourceById(String id, String value, DataFetchingEnvironment dfe) {
        return getArbeidsforholdstypeResource(
            endpoints.getAdministrasjonKodeverk() 
                + "/arbeidsforholdstype/" 
                + id 
                + "/" 
                + value,
            dfe);
    }

    public ArbeidsforholdstypeResource getArbeidsforholdstypeResource(String url, DataFetchingEnvironment dfe) {
        return webClientRequest.get(url, ArbeidsforholdstypeResource.class, dfe);
    }
}

