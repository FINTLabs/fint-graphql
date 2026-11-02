package model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Endpoints {

    @Value("${fint.endpoint.utdanning.elev:/utdanning/elev}")
    private String utdanningElev;

    public String getUtdanningElev() {
        return utdanningElev;
    }
}