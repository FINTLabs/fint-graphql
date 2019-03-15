// Built from tag release-3.2

package no.fint.graphql.model.utdanning.utdanningsprogram;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;

import no.fint.graphql.model.utdanning.skole.SkoleService;
import no.fint.graphql.model.utdanning.programomrade.ProgramomradeService;
import no.fint.graphql.model.utdanning.medlemskap.MedlemskapService;


import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.utdanningsprogram.UtdanningsprogramResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ProgramomradeResource;
import no.fint.model.resource.utdanning.elev.MedlemskapResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("utdanningUtdanningsprogramResolver")
public class UtdanningsprogramResolver implements GraphQLResolver<UtdanningsprogramResource> {

    @Autowired
    private SkoleService skoleService;

    @Autowired
    private ProgramomradeService programomradeService;

    @Autowired
    private MedlemskapService medlemskapService;


    public List<SkoleResource> getSkole(UtdanningsprogramResource utdanningsprogram, DataFetchingEnvironment dfe) {
        return utdanningsprogram.getSkole()
                .stream()
                .map(Link::getHref)
                .map(l -> skoleService.getSkoleResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<ProgramomradeResource> getProgramomrade(UtdanningsprogramResource utdanningsprogram, DataFetchingEnvironment dfe) {
        return utdanningsprogram.getProgramomrade()
                .stream()
                .map(Link::getHref)
                .map(l -> programomradeService.getProgramomradeResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<MedlemskapResource> getMedlemskap(UtdanningsprogramResource utdanningsprogram, DataFetchingEnvironment dfe) {
        return utdanningsprogram.getMedlemskap()
                .stream()
                .map(Link::getHref)
                .map(l -> medlemskapService.getMedlemskapResource(l, dfe))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}

