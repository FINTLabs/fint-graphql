package no.fint.graphql.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class XmiParserTest {
    @TempDir Path temporary;

    @Test
    void namespacesWindows1252NorwegianNamesAndLinearInheritanceAreReadDirectly() throws Exception {
        var xml = document(type("base", "Aktør", true, attribute("a", "fødselsdato", "date", "0", "1"))
                        + type("mid", "Mellom", true, attribute("b", "navn", "string", "1", "1"))
                        + type("leaf", "Lærling", false, attribute("c", "årstall", "int", "1", "*")),
                connector("Generalization", "leaf", "mid", "", "") + connector("Generalization", "mid", "base", "", ""));
        var model = parse(xml);
        var leaf = model.types().get(2);
        assertEquals("Larling", leaf.name);
        assertEquals("no.novari.fint.model.utdanning", leaf.packageName);
        assertEquals("arstall", leaf.attributes.getFirst().name());
        assertTrue(leaf.attributes.getFirst().list());
        assertFalse(leaf.attributes.getFirst().optional());
        assertEquals("navn", leaf.inheritedAttributes().get(0).name());
        assertEquals("fodselsdato", leaf.inheritedAttributes().get(1).name());
        assertTrue(leaf.inheritedAttributes().get(1).optional());
    }

    @Test
    void associationsUseIdsDespiteDuplicateSimpleNamesAndInheritInBothDirections() throws Exception {
        var model = parse(document(type("a", "Rolle", false, "") + type("b", "Rolle", false, "")
                        + type("base", "Base", true, "") + type("child", "Child", false, ""),
                connector("Association", "base", "a", "roller", "barn")
                        + connector("Association", "b", "base", "base", "andreRoller")
                        + connector("Generalization", "child", "base", "", "")));
        var child = model.types().get(3);
        assertEquals(2, child.relations().size());
        assertEquals("a", child.relations().get(0).target().id);
        assertEquals("b", child.relations().get(1).target().id);
        assertEquals("roller", child.relations().get(0).name());
        assertEquals("andreRoller", child.relations().get(1).name());
        assertTrue(child.relations().getFirst().list());
        assertTrue(child.relations().getFirst().optional());
    }

    @Test
    void attributeIdsDisambiguateTypesAndDanglingIdsCannotFallBackToSimpleNames() throws Exception {
        var xml = document(type("a", "Rolle", false, "") + type("b", "Rolle", false, "")
                + type("owner", "Owner", false, attribute("field", "rolle", "Rolle", "0", "1")), "");
        var property = "<ownedAttribute xmi:id=\"field\"><type xmi:idref=\"b\"/></ownedAttribute>";
        xml = xml.replace("<xmi:Extension", property + "<xmi:Extension");
        var model = parse(xml);
        assertEquals("b", model.types().get(2).attributes.getFirst().target().id);
        assertError(xml.replace("xmi:idref=\"b\"/></ownedAttribute>", "xmi:idref=\"missing\"/></ownedAttribute>"), "Unresolved attribute reference");
        var externalPrimitive = document(type("owner", "Owner", false, attribute("field", "flag", "boolean", "0", "1")), "")
                .replace("<xmi:Extension", "<ownedAttribute xmi:id=\"field\"><type href=\"uml-primitives#Boolean\"/></ownedAttribute><xmi:Extension");
        assertEquals("boolean", parse(externalPrimitive).types().getFirst().attributes.getFirst().type());
    }

    @Test
    void unknownReferencesCyclesAmbiguousAttributesAndMultipleParentsFail() {
        assertError(document(type("a", "A", false, ""), connector("Association", "a", "missing", "relation", "")), "Unresolved class reference");
        assertError(document(type("a", "A", false, "") + type("b", "B", false, ""),
                connector("Generalization", "a", "b", "", "") + connector("Generalization", "b", "a", "", "")), "Inheritance cycle");
        assertError(document(type("a", "A", false, attribute("f", "value", "Unknown", "0", "1")), ""), "Unresolved or ambiguous attribute");
        assertError(document(type("a", "A", false, "") + type("b", "B", false, "") + type("c", "C", false, ""),
                connector("Generalization", "a", "b", "", "") + connector("Generalization", "a", "c", "", "")), "Multiple parents");
        assertError(document(type("a", "A", false, ""), "").replace("package=\"domain\"", "package=\"missing\""), "Unresolved package");
        assertError(document(type("a", "A", false, ""), "").replace("<model package=\"\"/>", "<model package=\"domain\"/>"), "Package cycle");
    }

    @Test
    void malformedDocumentsAndExternalEntitiesAreRejected() {
        assertThrows(Exception.class, () -> parse("<broken>"));
        var xml = document(type("a", "A", false, ""), "");
        assertThrows(Exception.class, () -> parse(xml.replace("<xmi:XMI", "<!DOCTYPE xmi:XMI [<!ENTITY secret SYSTEM 'file:///etc/passwd'>]><xmi:XMI")));
        assertThrows(Exception.class, () -> parse("<empty/>"));
    }

    private void assertError(String xml, String message) {
        assertTrue(assertThrows(IllegalArgumentException.class, () -> parse(xml)).getMessage().contains(message));
    }
    private Model parse(String xml) throws Exception {
        Path file = temporary.resolve("model.xml");
        Files.writeString(file, xml, Charset.forName("windows-1252"));
        return new XmiParser().parse(file);
    }
    private static String document(String types, String connectors) {
        return """
                <?xml version="1.0" encoding="windows-1252"?>
                <xmi:XMI xmlns:xmi="http://schema.omg.org/spec/XMI/2.1" xmlns:uml="http://schema.omg.org/spec/UML/2.1">
                  <xmi:Extension extender="Enterprise Architect"><elements>
                    <element xmi:idref="domain" xmi:type="uml:Package" name="Utdanning"><model package=""/></element>
                    %s
                  </elements><connectors>%s</connectors></xmi:Extension>
                </xmi:XMI>
                """.formatted(types, connectors);
    }
    private static String type(String id, String name, boolean abstractType, String attributes) {
        return """
                <element xmi:idref="%s" xmi:type="uml:Class" name="%s">
                  <model package="domain"/><properties isAbstract="%s"/><attributes>%s</attributes>
                </element>
                """.formatted(id, name, abstractType, attributes);
    }
    private static String attribute(String id, String name, String type, String lower, String upper) {
        return "<attribute xmi:idref=\"%s\" name=\"%s\"><properties type=\"%s\"/><bounds lower=\"%s\" upper=\"%s\"/></attribute>".formatted(id, name, type, lower, upper);
    }
    private static String connector(String kind, String source, String target, String targetRole, String sourceRole) {
        return """
                <connector><properties ea_type="%s"/>
                  <source xmi:idref="%s"><role name="%s"/><type multiplicity="0..*"/></source>
                  <target xmi:idref="%s"><role name="%s"/><type multiplicity="0..*"/></target>
                </connector>
                """.formatted(kind, source, sourceRole, target, targetRole);
    }
}
