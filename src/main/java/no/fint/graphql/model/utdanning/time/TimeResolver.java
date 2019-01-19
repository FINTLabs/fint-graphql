// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.time;

import com.coxautodev.graphql.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.undervisningsgruppe.UndervisningsgruppeService;
import no.fint.graphql.model.utdanning.undervisningsforhold.UndervisningsforholdService;
import no.fint.graphql.model.utdanning.rom.RomService;


import no.fint.model.resource.utdanning.timeplan.TimeResource;


import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.timeplan.RomResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningTimeResolver")
public class TimeResolver implements GraphQLResolver<TimeResource> {


    @Autowired
    private UndervisningsgruppeService undervisningsgruppeService;

    @Autowired
    private UndervisningsforholdService undervisningsforholdService;

    @Autowired
    private RomService romService;


    public UndervisningsgruppeResource getUndervisningsgruppe(TimeResource time, DataFetchingEnvironment dfe) {
        return undervisningsgruppeService.getUndervisningsgruppeResource(
            Links.get(time.getUndervisningsgruppe()),
            dfe);
    }

    public UndervisningsforholdResource getUndervisningsforhold(TimeResource time, DataFetchingEnvironment dfe) {
        return undervisningsforholdService.getUndervisningsforholdResource(
            Links.get(time.getUndervisningsforhold()),
            dfe);
    }

    public RomResource getRom(TimeResource time, DataFetchingEnvironment dfe) {
        return romService.getRomResource(
            Links.get(time.getRom()),
            dfe);
    }

}

