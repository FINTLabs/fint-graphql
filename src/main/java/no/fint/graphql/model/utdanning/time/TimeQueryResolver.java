// Built from tag v3.1.0

package no.fint.graphql.model.utdanning.time;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import no.fint.model.resource.utdanning.timeplan.TimeResource;
import no.fint.model.resource.utdanning.timeplan.TimeResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("utdanningTimeQueryResolver")
public class TimeQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private TimeService service;

    public List<TimeResource> getTime(String sinceTimeStamp) {
        TimeResources resources = service.getTimeResources(sinceTimeStamp);
        return resources.getContent();
    }
}
