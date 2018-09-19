package no.fint.graphql.arbeidsforhold;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.personalressurs.PersonalressursService;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ArbeidsforholdResolver implements GraphQLResolver<ArbeidsforholdResource> {

    @Autowired
    private WebClient webClient;

    @Autowired
    private PersonalressursService personalressursService;

    public PersonalressursResource getPersonalressurs(ArbeidsforholdResource arbeidsforhold) {
        List<Link> personalressursLinks = arbeidsforhold.getLinks().get("personalressurs");
        if (personalressursLinks.isEmpty()) {
            return null;
        }

        return personalressursService.getPersonalressursResource(personalressursLinks.get(0).getHref());
    }
}
