
package no.fint.graphql.model.model.sensor;

import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.model.skoleressurs.SkoleressursService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.SensorResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Controller("modelSensorResolver")
public class SensorResolver {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;


    @SchemaMapping(typeName = "Sensor", field = "skoleressurs")
    public CompletionStage<SkoleressursResource> getSkoleressurs(SensorResource sensor, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sensor.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    @SchemaMapping(typeName = "Sensor", field = "eksamensgruppe")
    public CompletionStage<EksamensgruppeResource> getEksamensgruppe(SensorResource sensor, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sensor.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

