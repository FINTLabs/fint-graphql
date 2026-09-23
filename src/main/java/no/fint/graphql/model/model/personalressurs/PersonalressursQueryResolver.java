
package no.fint.graphql.model.model.personalressurs;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.personal.PersonalressursResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelPersonalressursQueryResolver")
@Slf4j
public class PersonalressursQueryResolver {

    @Autowired
    private PersonalressursService service;

    @QueryMapping(name = "personalressurs")
    public CompletionStage<PersonalressursResource> personalressurs(
            @Argument("ansattnummer") String ansattnummer,
            @Argument("brukernavn") String brukernavn,
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Personalressurs");
        if (StringUtils.isNotEmpty(ansattnummer)) {
            return service.getPersonalressursResourceById("ansattnummer", ansattnummer, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(brukernavn)) {
            return service.getPersonalressursResourceById("brukernavn", brukernavn, dfe).toFuture();
        }
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersonalressursResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<PersonalressursResource>empty().toFuture();
    }
}
