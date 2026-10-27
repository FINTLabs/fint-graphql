package no.fint.graphql;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** A finite set of model routes; request data is never used as a metric label. */
public class UriRouteCatalog {
    public static final String UNKNOWN = "/unknown";
    private static final String VALUE = "/{value}";
    private final List<Matcher> routes;

    public UriRouteCatalog(List<Route> routes) {
        this.routes = routes.stream().map(route -> {
            boolean lookup = route.path().endsWith(VALUE);
            String path = lookup ? route.path().substring(0, route.path().length() - VALUE.length()) : route.path();
            path = URI.create(path).getRawPath();
            if (!path.startsWith("/")) path = "/" + path;
            return new Matcher(lookup ? path + "/" : path, lookup, route.template());
        }).toList();
    }

    public String template(URI uri) {
        String path = uri.getRawPath();
        for (Matcher route : routes) {
            if (route.lookup() && path.startsWith(route.path())) {
                String value = path.substring(route.path().length());
                if (!value.isEmpty() && !value.contains("/")) {
                    return route.template();
                }
            } else if (!route.lookup() && route.path().equals(path)) {
                return route.template();
            }
        }
        return UNKNOWN;
    }

    public Set<String> templates() {
        return routes.stream().map(Matcher::template).collect(Collectors.toUnmodifiableSet());
    }

    private record Matcher(String path, boolean lookup, String template) { }

    public record Route(String path, String template) { }
}
