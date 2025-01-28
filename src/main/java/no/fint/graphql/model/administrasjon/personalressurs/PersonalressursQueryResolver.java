
package no.fint.graphql.model.administrasjon.personalressurs;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonPersonalressursQueryResolver")
@Slf4j
public class PersonalressursQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressursService service;

    public CompletionStage<PersonalressursResource> getPersonalressurs(
            String ansattnummer,
            String brukernavn,
            String systemId,
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
