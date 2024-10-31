
package no.fint.graphql.model.utdanning.eksamensgruppe;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.eksamen.EksamenService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.termin.TerminService;
import no.fint.graphql.model.utdanning.eksamensform.EksamensformService;
import no.fint.graphql.model.utdanning.skolear.SkolearService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.eksamensgruppemedlemskap.EksamensgruppemedlemskapService;
import no.fint.graphql.model.utdanning.sensor.SensorService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.timeplan.EksamenResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.fint.model.resource.utdanning.kodeverk.EksamensformResource;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppemedlemskapResource;
import no.fint.model.resource.utdanning.vurdering.SensorResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningEksamensgruppeResolver")
public class EksamensgruppeResolver implements GraphQLResolver<EksamensgruppeResource> {

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private EksamenService eksamenService;

    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private TerminService terminService;

    @Autowired
    private EksamensformService eksamensformService;

    @Autowired
    private SkolearService skolearService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private EksamensgruppemedlemskapService eksamensgruppemedlemskapService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private MedlemskapService medlemskapService;


    public CompletionStage<List<ElevforholdResource>> getElevforhold(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<EksamenResource> getEksamen(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getEksamen()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamenService.getEksamenResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<FagResource> getFag(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkoleResource> getSkole(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<TerminResource> getTermin(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getTermin()
                .stream()
                .map(Link::getHref)
                .map(l -> terminService.getTerminResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<EksamensformResource> getEksamensform(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getEksamensform()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensformService.getEksamensformResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<SkolearResource> getSkolear(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getSkolear()
                .stream()
                .map(Link::getHref)
                .map(l -> skolearService.getSkolearResource(l, dfe)))
                .flatMap(Mono::flux)
                .next()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppemedlemskapResource>> getGruppemedlemskap(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getGruppemedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppemedlemskapService.getEksamensgruppemedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SensorResource>> getSensor(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getSensor()
                .stream()
                .map(Link::getHref)
                .map(l -> sensorService.getSensorResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<MedlemskapResource>> getMedlemskap(EksamensgruppeResource eksamensgruppe, DataFetchingEnvironment dfe) {
        return Flux.fromStream(eksamensgruppe.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

