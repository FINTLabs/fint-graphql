package no.fint.graphql;

import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.function.Function;

public class ResourceUrlBuilder {

    public static Function<UriBuilder, URI> urlWithQueryParams(String path, String sinceTimeStamp) {
        return uriBuilder -> {
            UriBuilder builder = uriBuilder.path(path);
            return sinceTimeStamp == null ? builder.build() : builder.queryParam("sinceTimeStamp", sinceTimeStamp).build();
        };
    }
}
