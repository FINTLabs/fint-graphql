package no.fint.graphql;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class GraphQLQueryIdProvider {
    private final AtomicLong counter = new AtomicLong();

    public long nextId() {
        return counter.incrementAndGet();
    }
}
