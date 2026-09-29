package no.fint.graphql.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** The small part of the EA information model needed by GraphQL generation. */
public record Model(List<Type> types) {
    public Model { types = List.copyOf(types); }

    public static final class Type {
        final String id;
        final String name;
        final String packageName;
        final boolean abstractType;
        String stereotype;
        Type parent;
        final List<Attribute> attributes = new ArrayList<>();
        final List<Relation> outgoing = new ArrayList<>();
        final List<Relation> incoming = new ArrayList<>();

        Type(String id, String name, String packageName, boolean abstractType, String stereotype) {
            this.id = id;
            this.name = name;
            this.packageName = packageName;
            this.abstractType = abstractType;
            this.stereotype = stereotype;
        }

        List<Attribute> inheritedAttributes() {
            var result = new ArrayList<Attribute>();
            for (var type = parent; type != null; type = type.parent) result.addAll(type.attributes);
            return result;
        }

        List<Attribute> allAttributes() {
            var result = new ArrayList<>(attributes);
            result.addAll(inheritedAttributes());
            return result;
        }

        List<Attribute> identifiers() {
            return allAttributes().stream().filter(a -> a.type().equals("Identifikator")).toList();
        }

        List<Relation> relations() {
            if (abstractType) return List.of();
            var result = new ArrayList<Relation>();
            for (var type = this; type != null; type = type.parent) {
                result.addAll(type.outgoing);
                result.addAll(type.incoming);
            }
            return result;
        }

        boolean resource() { return !relations().isEmpty(); }
        boolean mainClass() { return stereotype.equals("hovedklasse"); }
        boolean included() {
            return packageName.startsWith("no.novari.fint.model.administrasjon.")
                    || packageName.equals("no.novari.fint.model.administrasjon")
                    || packageName.startsWith("no.novari.fint.model.utdanning.")
                    || packageName.equals("no.novari.fint.model.utdanning")
                    || packageName.startsWith("no.novari.fint.model.felles.")
                    || packageName.equals("no.novari.fint.model.felles");
        }
        boolean rootQuery() {
            return !abstractType && included() && mainClass() && !packageName.contains("kodeverk")
                    && identifiers().stream().anyMatch(a -> !a.optional());
        }
        String lowerName() { return name.toLowerCase(Locale.ROOT); }
        String javaPackage() { return "no.fint.graphql.model.model." + lowerName(); }
        String resourceClass() { return packageName.replace(".model.", ".model.resource.") + "." + name + "Resource"; }
        String endpointPath() { return packageName.substring("no.novari.fint.model.".length()).replace('.', '/'); }
    }

    public record Attribute(String name, String type, boolean list, boolean optional, Type owner, Type target) {}
    public record Relation(String name, Type target, boolean list, boolean optional) {}
}
