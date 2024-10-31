
package no.fint.graphql.model.utdanning.eksamensgruppemedlemskap;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.felles.fylke.FylkeService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.karakterstatus.KarakterstatusService;
import no.fint.graphql.model.utdanning.betalingsstatus.BetalingsstatusService;
import no.fint.graphql.model.utdanning.sensor.SensorService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import no.fint.model.resource.felles.kodeverk.FylkeResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.kodeverk.KarakterstatusResource;
import no.fint.model.resource.utdanning.kodeverk.BetalingsstatusResource;
import no.fint.model.resource.utdanning.vurdering.SensorResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningEksamensgruppemedlemskapResolver")
public class EksamensgruppemedlemskapResolver implements GraphQLResolver<EksamensgruppemedlemskapResource> {

    @Autowired
    private FylkeService fylkeService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private KarakterstatusService karakterstatusService;

    @Autowired
    private BetalingsstatusService betalingsstatusService;

    @Autowired
    private SensorService sensorService;


    public CompletionStage<FylkeResource> getDelegertTil(EksamensgruppemedlemskapResource eksamensgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppemedlemskap.getDelegertTil()
                .stream()
                .map(Link::getHref)
                .map(l -> fylkeService.getFylkeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<ElevforholdResource> getElevforhold(EksamensgruppemedlemskapResource eksamensgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppemedlemskap.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getForetrukketSkole(EksamensgruppemedlemskapResource eksamensgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppemedlemskap.getForetrukketSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<EksamensgruppeResource> getEksamensgruppe(EksamensgruppemedlemskapResource eksamensgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppemedlemskap.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<KarakterstatusResource> getNus(EksamensgruppemedlemskapResource eksamensgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppemedlemskap.getNus()
                .stream()
                .map(Link::getHref)
                .map(l -> karakterstatusService.getKarakterstatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<BetalingsstatusResource> getBetalingsstatus(EksamensgruppemedlemskapResource eksamensgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppemedlemskap.getBetalingsstatus()
                .stream()
                .map(Link::getHref)
                .map(l -> betalingsstatusService.getBetalingsstatusResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SensorResource> getForetrukketSensor(EksamensgruppemedlemskapResource eksamensgruppemedlemskap, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppemedlemskap.getForetrukketSensor()
                .stream()
                .map(Link::getHref)
                .map(l -> sensorService.getSensorResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

}

