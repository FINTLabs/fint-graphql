
package no.fint.graphql.model.utdanning.skole;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.organisasjonselement.OrganisasjonselementService;
import no.fint.graphql.model.utdanning.fag.FagService;
import no.fint.graphql.model.utdanning.skoleeiertype.SkoleeiertypeService;
import no.fint.graphql.model.utdanning.basisgruppe.BasisgruppeService;
import no.fint.graphql.model.utdanning.elevforhold.ElevforholdService;
import no.fint.graphql.model.utdanning.kontaktlarergruppe.KontaktlarergruppeService;
import no.fint.graphql.model.utdanning.skoleressurs.SkoleressursService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.eksamensgruppe.EksamensgruppeService;
import no.fint.graphql.model.utdanning.utdanningsprogram.UtdanningsprogramService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.kodeverk.SkoleeiertypeResource;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.KontaktlarergruppeResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.vurdering.EksamensgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletionStage;

@Component("utdanningSkoleResolver")
public class SkoleResolver implements GraphQLResolver<SkoleResource> {

    @Autowired
    private OrganisasjonselementService organisasjonselementService;

    @Autowired
    private FagService fagService;

    @Autowired
    private SkoleeiertypeService skoleeiertypeService;

    @Autowired
    private BasisgruppeService basisgruppeService;

    @Autowired
    private ElevforholdService elevforholdService;

    @Autowired
    private KontaktlarergruppeService kontaktlarergruppeService;

    @Autowired
    private SkoleressursService skoleressursService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private EksamensgruppeService eksamensgruppeService;

    @Autowired
    private UtdanningsprogramService utdanningsprogramService;


    public CompletionStage<OrganisasjonselementResource> getOrganisasjon(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getOrganisasjon()
                .stream()
                .map(Link::getHref)
                .map(l -> organisasjonselementService.getOrganisasjonselementResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<List<FagResource>> getFag(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getFag()
                .stream()
                .map(Link::getHref)
                .map(l -> fagService.getFagResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<SkoleeiertypeResource> getSkoleeierType(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getSkoleeierType()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleeiertypeService.getSkoleeiertypeResource(l, dfe)))
                .flatMap(Mono::flux)
                .singleOrEmpty()
                .toFuture();
    }

    public CompletionStage<List<BasisgruppeResource>> getBasisgruppe(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getBasisgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> basisgruppeService.getBasisgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<ElevforholdResource>> getElevforhold(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getElevforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> elevforholdService.getElevforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<KontaktlarergruppeResource>> getKontaktlarergruppe(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getKontaktlarergruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> kontaktlarergruppeService.getKontaktlarergruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<SkoleressursResource>> getSkoleressurs(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleressursService.getSkoleressursResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsforholdResource>> getUndervisningsforhold(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getUndervisningsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsforholdService.getUndervisningsforholdResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UndervisningsgruppeResource>> getUndervisningsgruppe(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> undervisningsgruppeService.getUndervisningsgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<EksamensgruppeResource>> getEksamensgruppe(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getEksamensgruppe()
                .stream()
                .map(Link::getHref)
                .map(l -> eksamensgruppeService.getEksamensgruppeResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

    public CompletionStage<List<UtdanningsprogramResource>> getUtdanningsprogram(SkoleResource skole, DataFetchingEnvironment dfe) {
        return Flux.fromStream(skole.getUtdanningsprogram()
                .stream()
                .map(Link::getHref)
                .map(l -> utdanningsprogramService.getUtdanningsprogramResource(l, dfe)))
                .flatMap(Mono::flux)
                .collectList()
                .toFuture();
    }

}

