// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.rom;

import com.coxautodev.graphql.tools.GraphQLResolver;
import no.fint.graphql.Links;




import no.fint.graphql.model.utdanning.time.TimeService;


import no.fint.model.resource.utdanning.timeplan.RomResource;


import no.fint.model.resource.utdanning.timeplan.TimeResource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("utdanningRomResolver")
public class RomResolver implements GraphQLResolver<RomResource> {

	
	@Autowired
	private TimeService timeService;
	
	


	
	
	public TimeResource getTime(RomResource rom) {
        return timeService.getTimeResource(Links.get(rom, "time"));
    }
	
	
}

