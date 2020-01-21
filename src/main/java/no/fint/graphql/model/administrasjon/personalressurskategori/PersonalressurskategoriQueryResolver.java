
package no.fint.graphql.model.administrasjon.personalressurskategori;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.model.resource.administrasjon.kodeverk.PersonalressurskategoriResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("administrasjonPersonalressurskategoriQueryResolver")
public class PersonalressurskategoriQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private PersonalressurskategoriService service;

    public CompletionStage<PersonalressurskategoriResource> getPersonalressurskategori(
            String systemId,
            DataFetchingEnvironment dfe) {
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getPersonalressurskategoriResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<PersonalressurskategoriResource>empty().toFuture();
    }
}
