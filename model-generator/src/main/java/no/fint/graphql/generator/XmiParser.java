package no.fint.graphql.generator;

import no.fint.graphql.generator.Model.Attribute;
import no.fint.graphql.generator.Model.Relation;
import no.fint.graphql.generator.Model.Type;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Reads the EA extension directly; no lossy namespace stripping or encoding conversion. */
public final class XmiParser {
    private static final String XMI = "http://schema.omg.org/spec/XMI/2.1";
    private static final Set<String> PRIMITIVES = Set.of("string", "boolean", "date", "datetime", "float", "double", "long", "int", "referanse");

    public Model parse(Path file) throws IOException {
        try {
            var factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setXIncludeAware(false);
            var document = factory.newDocumentBuilder().parse(file.toFile());
            Element extension = null;
            var extensions = document.getElementsByTagNameNS(XMI, "Extension");
            for (int i = 0; i < extensions.getLength(); i++) {
                var candidate = (Element) extensions.item(i);
                if (candidate.getAttribute("extender").equals("Enterprise Architect")) {
                    if (extension != null) throw invalid("Multiple Enterprise Architect extensions");
                    extension = candidate;
                }
            }
            if (extension == null) throw invalid("Missing Enterprise Architect extension");

            var elements = new LinkedHashMap<String, Element>();
            for (var element : children(child(extension, "elements"), "element")) {
                var id = xmi(element, "idref");
                if (id.isBlank() || elements.putIfAbsent(id, element) != null) throw invalid("Missing or duplicate element ID: " + id);
            }
            var types = new LinkedHashMap<String, Type>();
            for (var entry : elements.entrySet()) {
                var element = entry.getValue();
                if (!xmi(element, "type").equals("uml:Class")) continue;
                var properties = child(element, "properties");
                var name = normalize(element.getAttribute("name"));
                identifier(name);
                types.put(entry.getKey(), new Type(entry.getKey(), name,
                        packageName(element, elements), Boolean.parseBoolean(properties.getAttribute("isAbstract")),
                        properties.getAttribute("stereotype")));
            }
            if (types.isEmpty()) throw invalid("Information model contains no classes");

            // UML ownedAttribute IDs disambiguate types with the same simple name.
            var attributeTypes = new HashMap<String, String>();
            var primitiveIds = new HashSet<String>();
            var packaged = document.getElementsByTagName("packagedElement");
            for (int i = 0; i < packaged.getLength(); i++) {
                var element = (Element) packaged.item(i);
                if (xmi(element, "type").equals("uml:PrimitiveType")) primitiveIds.add(xmi(element, "id"));
            }
            var properties = document.getElementsByTagName("ownedAttribute");
            for (int i = 0; i < properties.getLength(); i++) {
                var property = (Element) properties.item(i);
                var type = optionalChild(property, "type");
                // Some EA primitive types use an external UML href instead of a local idref.
                if (type != null && !xmi(type, "idref").isBlank()) {
                    attributeTypes.put(xmi(property, "id"), xmi(type, "idref"));
                }
            }
            for (var type : types.values()) {
                var attributes = optionalChild(elements.get(type.id), "attributes");
                if (attributes == null) continue;
                for (var attribute : children(attributes, "attribute")) {
                    var name = normalize(attribute.getAttribute("name"));
                    identifier(name);
                    var rawType = normalize(child(attribute, "properties").getAttribute("type"));
                    String targetId = attributeTypes.get(xmi(attribute, "idref"));
                    Type target = types.get(targetId);
                    if (targetId != null && target == null && !primitiveIds.contains(targetId)) {
                        throw invalid("Unresolved attribute reference " + targetId + " in " + type.name + "." + name);
                    }
                    if (target == null && !PRIMITIVES.contains(rawType.toLowerCase(Locale.ROOT))) {
                        var matches = types.values().stream().filter(t -> t.name.equals(rawType)).toList();
                        if (matches.size() != 1) throw invalid("Unresolved or ambiguous attribute type " + rawType + " in " + type.name + "." + name);
                        target = matches.getFirst();
                    }
                    var bounds = child(attribute, "bounds");
                    boolean list = bounds.getAttribute("upper").equals("*");
                    type.attributes.add(new Attribute(name, target == null ? rawType : target.name, list,
                            !list && bounds.getAttribute("lower").equals("0"), type, target));
                }
            }

            for (var connector : children(child(extension, "connectors"), "connector")) {
                var kind = child(connector, "properties").getAttribute("ea_type");
                if (!kind.equals("Generalization") && !kind.equals("Association")) continue;
                var sourceElement = child(connector, "source");
                var targetElement = child(connector, "target");
                var source = requireType(types, xmi(sourceElement, "idref"));
                var target = requireType(types, xmi(targetElement, "idref"));
                if (kind.equals("Generalization")) {
                    if (source.parent != null) throw invalid("Multiple parents for " + source.name);
                    source.parent = target;
                } else {
                    addRelation(source.outgoing, targetElement, target);
                    addRelation(target.incoming, sourceElement, source);
                }
            }
            for (var type : types.values()) {
                var visited = new HashSet<String>();
                for (var ancestor = type; ancestor != null; ancestor = ancestor.parent) {
                    if (!visited.add(ancestor.id)) throw invalid("Inheritance cycle at " + type.name);
                }
            }
            for (var type : types.values()) {
                if (type.stereotype.isEmpty()) type.stereotype = type.abstractType ? "abstrakt"
                        : type.identifiers().isEmpty() ? "datatype" : "hovedklasse";
            }
            return new Model(new ArrayList<>(types.values()));
        } catch (IOException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Cannot parse XMI " + file + ": " + e.getMessage(), e);
        }
    }

    private static void addRelation(List<Relation> relations, Element end, Type target) {
        String name = normalize(child(end, "role").getAttribute("name"));
        if (name.isEmpty()) return;
        identifier(name);
        String multiplicity = child(end, "type").getAttribute("multiplicity");
        relations.add(new Relation(name, target, multiplicity.endsWith("*"), multiplicity.startsWith("0")));
    }

    private static Type requireType(Map<String, Type> types, String id) {
        var type = types.get(id);
        if (type == null) throw invalid("Unresolved class reference " + id);
        return type;
    }

    private static String packageName(Element element, Map<String, Element> elements) {
        var segments = new ArrayList<String>();
        var visited = new HashSet<String>();
        String id = child(element, "model").getAttribute("package");
        while (!id.isEmpty()) {
            if (!visited.add(id)) throw invalid("Package cycle at " + id);
            var parent = elements.get(id);
            if (parent == null) throw invalid("Unresolved package " + id);
            String name = parent.getAttribute("name");
            if (!name.equals("FINT") && !name.equals("Model")) {
                name = normalize(name.replace(" ", "")).toLowerCase(Locale.ROOT);
                identifier(name);
                segments.addFirst(name);
            }
            id = child(parent, "model").getAttribute("package");
        }
        return "no.novari.fint.model." + String.join(".", segments);
    }

    static String normalize(String text) {
        return text.replace('æ', 'a').replace('ø', 'o').replace('å', 'a')
                .replace('Æ', 'A').replace('Ø', 'O').replace('Å', 'A');
    }

    private static void identifier(String value) {
        if (!value.matches("[A-Za-z_][A-Za-z_0-9]*")) throw invalid("Invalid generated identifier: " + value);
    }

    private static String xmi(Element element, String name) { return element.getAttributeNS(XMI, name); }
    private static Element child(Element element, String name) {
        var child = optionalChild(element, name);
        if (child == null) throw invalid("Missing " + name + " in " + element.getTagName());
        return child;
    }
    private static Element optionalChild(Element element, String name) {
        return children(element, name).stream().findFirst().orElse(null);
    }
    private static List<Element> children(Element element, String name) {
        var result = new ArrayList<Element>();
        for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node instanceof Element child && name.equals(child.getLocalName())) result.add(child);
        }
        return result;
    }
    private static IllegalArgumentException invalid(String message) { return new IllegalArgumentException(message); }
}
