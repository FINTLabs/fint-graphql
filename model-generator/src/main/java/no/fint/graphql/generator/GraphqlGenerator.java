package no.fint.graphql.generator;

import no.fint.graphql.generator.Model.Attribute;
import no.fint.graphql.generator.Model.Relation;
import no.fint.graphql.generator.Model.Type;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/** Pure model-to-text transformation. Paths are relative to the legacy output layout. */
public final class GraphqlGenerator {
    public record Options(Set<String> excluded, Set<String> excludedSchema, Set<String> manualServices) {
        public Options {
            excluded = lower(excluded);
            excludedSchema = lower(excludedSchema);
            manualServices = Set.copyOf(manualServices);
        }
        public static Options application() {
            return new Options(Set.of("Fravar", "Fravarstype"), Set.of(), Set.of("no.novari.fint.model.felles.Person"));
        }
        private static Set<String> lower(Set<String> values) {
            return values.stream().map(s -> s.toLowerCase(Locale.ROOT)).collect(Collectors.toUnmodifiableSet());
        }
        boolean excluded(String name) { return excluded.contains(name.toLowerCase(Locale.ROOT)); }
        boolean schema(Type type) { return !excludedSchema.contains(type.lowerName()); }
    }

    public Map<String, String> generate(Model model, Options options) {
        var result = new TreeMap<String, String>();
        var selected = model.types().stream().filter(t -> !t.abstractType && t.included() && !options.excluded(t.name)).toList();
        var names = new HashMap<String, Type>();
        var roots = new ArrayList<Type>();
        var endpoints = new LinkedHashSet<String>();
        for (var type : selected) {
            var previous = names.putIfAbsent(type.lowerName(), type);
            if (previous != null) throw new IllegalArgumentException("Generated name collision: " + previous.packageName + "." + previous.name + " and " + type.packageName + "." + type.name);
            var attributes = type.attributes.stream().filter(a -> !options.excluded(a.type())).toList();
            var inherited = type.inheritedAttributes().stream().filter(a -> !options.excluded(a.type())).toList();
            var relations = type.relations().stream().filter(r -> !options.excluded(r.target().name)).toList();
            validateFields(type, attributes, inherited, relations);
            String prefix = "model/model/" + type.lowerName() + "/" + type.name;
            if (options.schema(type) && !(attributes.isEmpty() && inherited.isEmpty() && relations.isEmpty())) {
                result.put("schema/model/" + type.lowerName() + ".graphqls", schema(type, attributes, inherited, relations));
                if (type.rootQuery()) roots.add(type);
            }
            if (type.mainClass()) {
                result.put(prefix + "QueryResolver.java", query(type, type.rootQuery() && options.schema(type)));
                if (!options.manualServices().contains(type.packageName + "." + type.name)) result.put(prefix + "Service.java", service(type));
                endpoints.add(type.endpointPath());
            }
            if (type.resource()) result.put(prefix + "Resolver.java", resolver(type, relations));
        }
        for (var manual : options.manualServices()) {
            if (selected.stream().noneMatch(t -> t.mainClass() && manual.equals(t.packageName + "." + t.name))) {
                throw new IllegalArgumentException("Manual service no longer matches a generated main class: " + manual);
            }
        }
        roots.sort(Comparator.comparing(t -> t.name));
        var root = new StringBuilder("# java.util.Date implementation\nscalar Date\nscalar Long\n\ntype Query {\n");
        for (var type : roots) root.append("    ").append(type.lowerName()).append('(')
                .append(type.identifiers().stream().map(a -> a.name() + ": String").collect(Collectors.joining(", ")))
                .append("): ").append(type.name).append('\n');
        result.put("schema/root.graphqls", root.append("}\n").toString());
        result.put("model/Endpoints.java", endpoints(endpoints));
        return result;
    }

    private static void validateFields(Type type, List<Attribute> attributes, List<Attribute> inherited, List<Relation> relations) {
        var fields = new HashSet<String>();
        var names = new ArrayList<String>();
        attributes.forEach(a -> names.add(a.name()));
        inherited.forEach(a -> names.add(a.name()));
        relations.forEach(r -> names.add(r.name()));
        for (var name : names) if (!fields.add(name)) throw new IllegalArgumentException("Duplicate field " + type.name + "." + name);
    }

    private static String schema(Type type, List<Attribute> attributes, List<Attribute> inherited, List<Relation> relations) {
        var text = new StringBuilder("type " + type.name + " {\n");
        for (var attribute : attributes) field(text, attribute);
        for (var attribute : inherited) field(text, attribute);
        for (var relation : relations) {
            String value = relation.target().mainClass() ? wrap(relation.target().name, relation.list(), relation.optional()) : "[String]";
            text.append("    ").append(relation.name()).append(": ").append(value).append('\n');
        }
        return text.append("}\n").toString();
    }

    private static void field(StringBuilder text, Attribute attribute) {
        String scalar = switch (attribute.type().toLowerCase(Locale.ROOT)) {
            case "string", "referanse" -> "String";
            case "boolean" -> "Boolean";
            case "date", "datetime" -> "Date";
            case "float" -> "Float";
            case "double" -> "Double";
            case "long" -> "Long";
            case "int" -> "Int";
            default -> attribute.type();
        };
        text.append("    ").append(attribute.name()).append(": ").append(wrap(scalar, attribute.list(), attribute.optional())).append('\n');
    }

    private static String wrap(String name, boolean list, boolean optional) {
        return (list ? "[" + name + "]" : name) + (optional ? "" : "!");
    }

    private static String query(Type type, boolean publicQuery) {
        var imports = new ArrayList<>(List.of("graphql.schema.DataFetchingEnvironment", "lombok.extern.slf4j.Slf4j",
                type.resourceClass(), "org.springframework.beans.factory.annotation.Autowired", "org.springframework.stereotype.Controller",
                "reactor.core.publisher.Mono", "java.util.concurrent.CompletionStage"));
        if (!type.identifiers().isEmpty()) imports.add("org.apache.commons.lang3.StringUtils");
        if (publicQuery) {
            imports.add("org.springframework.graphql.data.method.annotation.QueryMapping");
            if (!type.identifiers().isEmpty()) imports.add("org.springframework.graphql.data.method.annotation.Argument");
        }
        var text = new StringBuilder("""
                package %s;

                %s

                @Controller("model%sQueryResolver")
                @Slf4j
                public class %sQueryResolver {
                    @Autowired
                    private %sService service;

                """.formatted(type.javaPackage(), imports(imports), type.name, type.name, type.name));
        if (publicQuery) text.append("    @QueryMapping(name = \"").append(type.lowerName()).append("\")\n");
        text.append("    public CompletionStage<").append(type.name).append("Resource> ").append(type.lowerName()).append("(\n");
        for (var id : type.identifiers()) text.append("            ")
                .append(publicQuery ? "@Argument(\"" + id.name() + "\") " : "")
                .append("String ").append(id.name()).append(",\n");
        text.append("            DataFetchingEnvironment dfe) {\n        log.info(\"New Query for ").append(type.name).append("\");\n");
        for (var id : type.identifiers()) text.append("""
                        if (StringUtils.isNotEmpty(%s)) {
                            return service.get%sResourceById("%s", %s, dfe).toFuture();
                        }
                """.formatted(id.name(), type.name, id.name().toLowerCase(Locale.ROOT), id.name()));
        return text.append("        return Mono.<").append(type.name).append("Resource>empty().toFuture();\n    }\n}\n").toString();
    }

    private static String service(Type type) {
        boolean feide = type.allAttributes().stream().anyMatch(a -> a.name().equals("feidenavn") && a.type().equals("Identifikator") && a.optional() && !a.list());
        return """
                package %s;

                import graphql.schema.DataFetchingEnvironment;
                import no.fint.graphql.WebClientRequest;
                import no.fint.graphql.model.Endpoints;
                import %s;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.stereotype.Service;
                %simport reactor.core.publisher.Mono;

                @Service("model%sService")
                public class %sService {
                    @Autowired
                    private WebClientRequest webClientRequest;
                    @Autowired
                    private Endpoints endpoints;

                    public Mono<%sResource> get%sResourceById(String id, String value, DataFetchingEnvironment dfe) {
                        return get%sResource(
                            endpoints.get%s() + "/%s/" + id + "/" + value,
                            dfe);
                    }

                    public Mono<%sResource> get%sResource(String url, DataFetchingEnvironment dfe) {
                        return webClientRequest.get(url, %sResource.class, dfe)%s;
                    }
                }
                """.formatted(type.javaPackage(), type.resourceClass(), feide ? "import org.springframework.util.StringUtils;\n" : "",
                type.name, type.name, type.name, type.name, type.name, upper(endpointName(type.endpointPath())), type.lowerName(),
                type.name, type.name, type.name, feide ? """
                .map(resource -> {
                            if (resource.getFeidenavn() != null && !StringUtils.hasText(resource.getFeidenavn().getIdentifikatorverdi())) {
                                resource.setFeidenavn(null);
                            }
                            return resource;
                        })""" : "");
    }

    private static String resolver(Type type, List<Relation> allRelations) {
        var relations = allRelations.stream().filter(r -> r.target().mainClass()).toList();
        var imports = new ArrayList<>(List.of("org.springframework.stereotype.Controller"));
        var targets = new LinkedHashSet<Type>();
        for (var relation : relations) {
            var target = relation.target();
            targets.add(target);
            if (target != type) imports.add(target.javaPackage() + "." + target.name + "Service");
            imports.add(target.resourceClass());
        }
        if (!relations.isEmpty()) imports.addAll(List.of("graphql.schema.DataFetchingEnvironment", "no.novari.fint.model.resource.Link",
                type.resourceClass(), "org.springframework.beans.factory.annotation.Autowired", "org.springframework.graphql.data.method.annotation.SchemaMapping",
                "reactor.core.publisher.Flux", "reactor.core.publisher.Mono", "java.util.concurrent.CompletionStage"));
        if (relations.stream().anyMatch(Relation::list)) imports.addAll(List.of("org.springframework.web.reactive.function.client.WebClientResponseException",
                "java.util.List", "java.util.Optional", "java.util.concurrent.CompletableFuture", "java.util.stream.Collectors"));
        var text = new StringBuilder("package " + type.javaPackage() + ";\n\n" + imports(imports)
                + "\n\n@Controller(\"model" + type.name + "Resolver\")\npublic class " + type.name + "Resolver {\n");
        for (var target : targets) text.append("    @Autowired\n    private ").append(target.name).append("Service ").append(target.lowerName()).append("Service;\n\n");
        for (var relation : relations) {
            String resource = relation.target().name + "Resource";
            text.append("""
                        @SchemaMapping(typeName = "%s", field = "%s")
                        public CompletionStage<%s> get%s(%sResource %s, DataFetchingEnvironment dfe) {
                    """.formatted(type.name, relation.name(), relation.list() ? "List<" + resource + ">" : resource,
                    upper(relation.name()), type.name, type.lowerName()));
            if (relation.list()) text.append("""
                            var links = Optional.ofNullable(%s.get%s()).orElseGet(List::of);
                            if (links.isEmpty()) {
                                return CompletableFuture.completedFuture(List.of());
                            }
                            return Flux.fromIterable(links)
                                    .map(Link::getHref)
                                    .flatMapSequential(href -> %sService.get%sResource(href, dfe)
                                            .map(Optional::of)
                                            .defaultIfEmpty(Optional.empty())
                                            .onErrorResume(WebClientResponseException.class,
                                                    ex -> Mono.just(Optional.empty())),
                                            8, 1)
                                    .collectList()
                                    .map(list -> list.stream()
                                            .map(opt -> opt.orElse(null))
                                            .collect(Collectors.toList()))
                                    .toFuture();
                    """.formatted(type.lowerName(), upper(relation.name()), relation.target().lowerName(), relation.target().name));
            else text.append("""
                            return Flux.fromStream(%s.get%s()
                                    .stream()
                                    .map(Link::getHref)
                                    .map(l -> %sService.get%sResource(l, dfe)))
                                    .flatMap(Mono::flux)
                                    .next()
                                    .toFuture();
                    """.formatted(type.lowerName(), upper(relation.name()), relation.target().lowerName(), relation.target().name));
            text.append("    }\n\n");
        }
        return text.append("}\n").toString();
    }

    static String imports(List<String> names) {
        var unique = new TreeSet<>(names);
        var resources = new TreeMap<String, List<String>>();
        for (var name : unique) if (name.startsWith("no.novari.fint.model.resource.") && name.endsWith("Resource")) {
            resources.computeIfAbsent(name.substring(0, name.lastIndexOf('.')), _ -> new ArrayList<>()).add(name);
        }
        resources.forEach((pkg, classes) -> {
            if (classes.size() >= 5) { unique.removeAll(classes); unique.add(pkg + ".*"); }
        });
        String ordinary = unique.stream().filter(s -> !s.startsWith("java.")).map(s -> "import " + s + ";").collect(Collectors.joining("\n"));
        String java = unique.stream().filter(s -> s.startsWith("java.")).map(s -> "import " + s + ";").collect(Collectors.joining("\n"));
        return ordinary + (java.isEmpty() ? "" : "\n\n" + java);
    }

    private static String endpoints(Set<String> paths) {
        var text = new StringBuilder("""
                package no.fint.graphql.model;

                import lombok.Getter;
                import org.springframework.beans.factory.annotation.Value;
                import org.springframework.stereotype.Component;

                @Getter
                @Component
                public class Endpoints {
                """);
        for (var path : paths) text.append("\n    @Value(\"${fint.endpoint.").append(path.replace('/', '.')).append(":/")
                .append(path).append("}\")\n    private String ").append(endpointName(path)).append(";\n");
        return text.append("}\n").toString();
    }

    private static String endpointName(String path) {
        var parts = path.split("/");
        var result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) result.append(upper(parts[i]));
        return result.toString();
    }
    private static String upper(String value) { return Character.toUpperCase(value.charAt(0)) + value.substring(1); }
}
