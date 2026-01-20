
package no.fint.graphql.model.model.personalressurskategori;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelPersonalressurskategoriQueryResolver")
@Slf4j
public class PersonalressurskategoriQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressurskategoriService service;

    public CompletionStage<PersonalressurskategoriResource> getPersonalressurskategori(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Personalressurskategori");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersonalressurskategoriResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<PersonalressurskategoriResource>empty().toFuture();
    }
}
