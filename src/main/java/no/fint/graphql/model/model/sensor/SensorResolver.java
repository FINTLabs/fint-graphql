
package no.fint.graphql.model.model.sensor;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.model.model.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.model.skoleressurs.SkoleressursService;
import no.novari.fint.model.resource.Link;
import no.novari.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.novari.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.novari.fint.model.resource.utdanning.vurdering.SensorResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletionStage;

@Component("modelSensorResolver")
public class SensorResolver implements GraphQLResolver<SensorResource> {

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;


    public CompletionStage<SkoleressursResource> getSkoleressurs(SensorResource sensor, DataFetchingEnvironment dfe) {
        return Flux.fromStream(sensor.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

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

