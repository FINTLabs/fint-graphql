// Built from tag v3.1.0

package no.fint.graphql.model.administrasjon.arbeidsforholdstype;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResource;
import no.fint.model.resource.administrasjon.kodeverk.ArbeidsforholdstypeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("administrasjonArbeidsforholdstypeQueryResolver")
public class ArbeidsforholdstypeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ArbeidsforholdstypeService service;

    public List<ArbeidsforholdstypeResource> getArbeidsforholdstype(String sinceTimeStamp, DataFetchingEnvironment dfe) {
        ArbeidsforholdstypeResources resources = service.getArbeidsforholdstypeResources(sinceTimeStamp, dfe);
        return resources.getContent();
    }
}
