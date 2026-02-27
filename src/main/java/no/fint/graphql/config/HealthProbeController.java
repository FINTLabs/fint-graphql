package no.fint.graphql.config;

import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/actuator/health")
public class HealthProbeController {

    private final HealthEndpoint healthEndpoint;

    public HealthProbeController(HealthEndpoint healthEndpoint) {
        this.healthEndpoint = healthEndpoint;
    }

    @GetMapping("/liveness")
    public ResponseEntity<Map<String, Object>> liveness() {
        return probeResponse();
    }

    @GetMapping("/readiness")
    public ResponseEntity<Map<String, Object>> readiness() {
        return probeResponse();
    }

    @GetMapping("/startup")
    public ResponseEntity<Map<String, Object>> startup() {
        return probeResponse();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        return probeResponse();
    }

    private ResponseEntity<Map<String, Object>> probeResponse() {
        HealthComponent health = healthEndpoint.health();
        HttpStatus status = health.getStatus() == Status.UP ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", health.getStatus().getCode());
        // Align with Spring Boot 2.3+ health payload shape (status + components)
        body.put("components", Collections.singletonMap("ping", Collections.singletonMap("status", "UP")));
        return ResponseEntity.status(status).body(body);
    }
}
