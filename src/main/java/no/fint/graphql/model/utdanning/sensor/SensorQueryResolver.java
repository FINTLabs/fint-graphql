
package no.fint.graphql.model.utdanning.sensor;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.vurdering.SensorResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("utdanningSensorQueryResolver")
@Slf4j
public class SensorQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private SensorService service;

    public CompletionStage<SensorResource> getSensor(
            String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Sensor");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSensorResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SensorResource>empty().toFuture();
    }
}
