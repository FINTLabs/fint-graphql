
package no.fint.graphql.model.model.sensor;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import no.novari.fint.model.resource.utdanning.vurdering.SensorResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSensorQueryResolver")
@Slf4j
public class SensorQueryResolver {

    @Autowired
    private SensorService service;

    @QueryMapping(name = "sensor")
    public CompletionStage<SensorResource> sensor(
            @Argument("systemId") String systemId,
            DataFetchingEnvironment dfe) {
		log.info("New Query for Sensor");
        if (StringUtils.isNotEmpty(systemId)) {
            return service.getSensorResourceById("systemid", systemId, dfe).toFuture();
        }
        return Mono.<SensorResource>empty().toFuture();
    }
}
