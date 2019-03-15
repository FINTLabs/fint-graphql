// Built from tag release-3.2

package no.fint.graphql.model.administrasjon.fravar;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.administrasjon.fravarsgrunn.FravarsgrunnService;
import no.fint.graphql.model.administrasjon.fravarstype.FravarstypeService;
import no.fint.graphql.model.administrasjon.arbeidsforhold.ArbeidsforholdService;
import no.fint.graphql.model.administrasjon.fravar.FravarService;


import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.FravarResource;
import no.fint.model.resource.administrasjon.kodeverk.FravarsgrunnResource;
import no.fint.model.resource.administrasjon.kodeverk.FravarstypeResource;
import no.fint.model.resource.administrasjon.personal.ArbeidsforholdResource;
import no.fint.model.resource.administrasjon.personal.FravarResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("administrasjonFravarResolver")
public class FravarResolver implements GraphQLResolver<FravarResource> {

    @Autowired
    private FravarsgrunnService fravarsgrunnService;

    @Autowired
    private FravarstypeService fravarstypeService;

    @Autowired
    private ArbeidsforholdService arbeidsforholdService;

    @Autowired
    private FravarService fravarService;


    public FravarsgrunnResource getFravarsgrunn(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getFravarsgrunn()
                .stream()
                .map(Link::getHref)
                .map(l -> fravarsgrunnService.getFravarsgrunnResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public FravarstypeResource getFravarstype(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getFravarstype()
                .stream()
                .map(Link::getHref)
                .map(l -> fravarstypeService.getFravarstypeResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public List<ArbeidsforholdResource> getArbeidsforhold(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getArbeidsforhold()
                .stream()
                .map(Link::getHref)
                .map(l -> arbeidsforholdService.getArbeidsforholdResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public FravarResource getFortsettelse(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getFortsettelse()
                .stream()
                .map(Link::getHref)
                .map(l -> fravarService.getFravarResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    public FravarResource getFortsetter(FravarResource fravar, DataFetchingEnvironment dfe) {
        return fravar.getFortsetter()
                .stream()
                .map(Link::getHref)
                .map(l -> fravarService.getFravarResource(l, dfe))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

}

