package no.fint.graphql.generator;

import no.fint.graphql.generator.Model.Attribute;
import no.fint.graphql.generator.Model.Relation;
import no.fint.graphql.generator.Model.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphqlGeneratorTest {
    private static final GraphqlGenerator.Options ALL = new GraphqlGenerator.Options(Set.of(), Set.of(), Set.of());
    @TempDir Path temporary;

    @Test
    void rootsAndExplicitMappingsUseTheSameEligibilityAndIdentifierOrder() {
        var type = main("OtUngdom");
        type.attributes.add(attribute(type, "fodselsnummer", "Identifikator", false, true));
        var files = generate(type);
        var query = files.get("model/model/otungdom/OtUngdomQueryResolver.java");
        assertTrue(query.contains("@QueryMapping(name = \"otungdom\")"));
        assertTrue(query.contains("@Argument(\"systemId\") String systemId"));
        assertTrue(query.contains("@Argument(\"fodselsnummer\") String fodselsnummer"));
        assertTrue(query.contains("getOtUngdomResourceById(\"systemid\", systemId, dfe)"));
        assertTrue(query.contains("getOtUngdomResourceById(\"fodselsnummer\", fodselsnummer, dfe)"));
        assertTrue(files.get("schema/root.graphqls").contains("otungdom(systemId: String, fodselsnummer: String): OtUngdom"));
        var hidden = new GraphqlGenerator().generate(new Model(List.of(type)), new GraphqlGenerator.Options(Set.of(), Set.of("otUNGDOM"), Set.of()));
        assertFalse(hidden.get("schema/root.graphqls").contains("otungdom("));
        assertFalse(hidden.get("model/model/otungdom/OtUngdomQueryResolver.java").contains("QueryMapping"));
        assertFalse(hidden.get("model/model/otungdom/OtUngdomQueryResolver.java").contains("Argument"));
    }

    @Test
    void codeListsAndOptionalOnlyIdentifiersDoNotExposeRootQueries() {
        var code = new Type("code", "Funksjon", "no.novari.fint.model.administrasjon.kodeverk", false, "hovedklasse");
        code.attributes.add(attribute(code, "systemId", "Identifikator", false, false));
        assertFalse(generate(code).get("model/model/funksjon/FunksjonQueryResolver.java").contains("QueryMapping"));
        var optional = main("Optional");
        optional.attributes.clear();
        optional.attributes.add(attribute(optional, "systemId", "Identifikator", false, true));
        assertFalse(generate(optional).get("model/model/optional/OptionalQueryResolver.java").contains("QueryMapping"));
        optional.attributes.clear();
        assertFalse(generate(optional).get("model/model/optional/OptionalQueryResolver.java").contains("StringUtils"));
    }

    @Test
    void relationshipListsKeepOrderNullSlotsAndBoundedConcurrencyWithPreciseImports() {
        var source = main("Elev");
        var target = main("Elevforhold");
        source.outgoing.add(new Relation("elevforhold", target, true, true));
        source.outgoing.add(new Relation("foreldre", source, true, true));
        var resolver = generate(source).get("model/model/elev/ElevResolver.java");
        for (var text : List.of("@SchemaMapping(typeName = \"Elev\", field = \"elevforhold\")", "flatMapSequential", "8, 1)",
                ".defaultIfEmpty(Optional.empty())", ".onErrorResume(WebClientResponseException.class", "opt.orElse(null)", "CompletableFuture.completedFuture(List.of())")) {
            assertTrue(resolver.contains(text), text);
        }
        assertFalse(resolver.contains("import no.fint.graphql.model.model.elev.ElevService;"));
        source.outgoing.clear();
        source.outgoing.add(new Relation("registrertAv", target, false, true));
        resolver = generate(source).get("model/model/elev/ElevResolver.java");
        assertTrue(resolver.contains("getRegistrertAv("));
        assertTrue(resolver.contains(".next()"));
        for (var unused : List.of("java.util.List", "Optional;", "CompletableFuture;", "Collectors;", "WebClientResponseException;")) assertFalse(resolver.contains(unused), unused);
    }

    @Test
    void feideNormalizationIncludesInheritedOptionalIdentifiersOnly() {
        var parent = main("Base");
        parent.attributes.add(attribute(parent, "feidenavn", "Identifikator", false, true));
        var child = main("Skoleressurs");
        child.attributes.clear();
        child.parent = parent;
        var service = generate(child).get("model/model/skoleressurs/SkoleressursService.java");
        assertTrue(service.contains("resource.setFeidenavn(null)"));
        assertTrue(service.contains("!StringUtils.hasText(resource.getFeidenavn().getIdentifikatorverdi())"));
        parent.attributes.removeLast();
        parent.attributes.add(attribute(parent, "feidenavn", "Identifikator", false, false));
        assertFalse(generate(child).get("model/model/skoleressurs/SkoleressursService.java").contains("StringUtils"));
    }

    @Test
    void scalarCardinalityInheritanceAndExclusionsArePreserved() {
        var base = new Type("base", "Base", "no.novari.fint.model.felles", true, "abstrakt");
        base.attributes.add(attribute(base, "belop", "long", false, false));
        var child = main("Elev");
        child.parent = base;
        child.attributes.add(attribute(child, "datoer", "datetime", true, false));
        var excluded = main("Fravar");
        child.attributes.add(new Attribute("fravar", "Fravar", false, true, child, excluded));
        child.outgoing.add(new Relation("fravarsliste", excluded, true, true));
        var empty = new Type("empty", "Grepreferanse", "no.novari.fint.model.felles", false, "referanse");
        var files = new GraphqlGenerator().generate(new Model(List.of(base, child, excluded, empty)),
                new GraphqlGenerator.Options(Set.of("fRAVAR"), Set.of(), Set.of()));
        var schema = files.get("schema/model/elev.graphqls");
        assertTrue(schema.contains("belop: Long!"));
        assertTrue(schema.contains("datoer: [Date]!"));
        assertFalse(schema.contains("fravar"));
        assertFalse(files.containsKey("schema/model/fravar.graphqls"));
        assertFalse(files.containsKey("schema/model/grepreferanse.graphqls"));
        assertTrue(files.get("schema/root.graphqls").contains("scalar Date\nscalar Long"));
    }

    @Test
    void lookupsSeparateRawValuesAndRoutesIncludeInheritedIdentifiersAndPersonSources() {
        var parent = main("Base");
        var child = main("Elev");
        child.attributes.clear();
        child.parent = parent;
        var files = generate(child);
        var service = files.get("model/model/elev/ElevService.java");
        assertTrue(service.contains("endpoints.getUtdanningElev() + \"/elev/\" + id + \"/{value}\""));
        assertTrue(service.contains("Map.of(\"value\", value), ElevResource.class, dfe)"));
        assertTrue(service.contains("normalize(webClientRequest.get(url, ElevResource.class, dfe))"));
        assertFalse(service.contains("+ value"));
        var routes = files.get("model/ResourceRoutes.java");
        assertTrue(routes.contains("\"/utdanning/elev/elev\""));
        assertTrue(routes.contains("\"/utdanning/elev/elev/systemid/{value}\""));
        assertTrue(routes.contains("environment.getProperty(\"fint.endpoint.utdanning.elev\""));

        var person = new Type("person", "Person", "no.novari.fint.model.felles", false, "hovedklasse");
        person.attributes.add(attribute(person, "fodselsnummer", "Identifikator", false, false));
        routes = generate(person).get("model/ResourceRoutes.java");
        assertTrue(routes.contains("\"/administrasjon/personal/person/fodselsnummer/{value}\""));
        assertTrue(routes.contains("\"/utdanning/elev/person/fodselsnummer/{value}\""));
    }

    @Test
    void resourceImportThresholdDeduplicatesAndSorts() {
        String pkg = "no.novari.fint.model.resource.administrasjon.kodeverk.";
        var resources = List.of(pkg + "ArtResource", pkg + "AnleggResource", pkg + "AnsvarResource", pkg + "FunksjonResource", pkg + "RammeResource");
        assertFalse(GraphqlGenerator.imports(resources.subList(0, 4)).contains(".*;"));
        assertEquals("import " + pkg + "*;", GraphqlGenerator.imports(resources));
        assertEquals("import java.util.List;".trim(), GraphqlGenerator.imports(List.of("java.util.List", "java.util.List")).trim());
    }

    @Test
    void collisionsAndStaleManualOverridesFailExplicitly() {
        var first = main("Rolle");
        var second = new Type("other", "Rolle", "no.novari.fint.model.felles", false, "hovedklasse");
        assertTrue(assertThrows(IllegalArgumentException.class, () -> new GraphqlGenerator().generate(new Model(List.of(first, second)), ALL)).getMessage().contains("collision"));
        assertThrows(IllegalArgumentException.class, () -> new GraphqlGenerator().generate(new Model(List.of(first)), GraphqlGenerator.Options.application()));
        first.attributes.add(first.attributes.getFirst());
        assertThrows(IllegalArgumentException.class, () -> generate(first));
    }

    @Test
    void outputReplacementRemovesObsoleteFilesAndRejectsEscapingPaths() throws Exception {
        var output = temporary.resolve("generated");
        GenerateGraphql.write(Map.of("schema/old.graphqls", "old"), output);
        GenerateGraphql.write(Map.of("schema/new.graphqls", "new"), output);
        assertFalse(Files.exists(output.resolve("resources/schema/old.graphqls")));
        assertEquals("new", Files.readString(output.resolve("resources/schema/new.graphqls")));
        assertThrows(Exception.class, () -> GenerateGraphql.write(Map.of("schema/../../../../outside", "bad"), output));
        assertEquals("new", Files.readString(output.resolve("resources/schema/new.graphqls")), "Invalid generation preserves previous output");
    }

    private static Type main(String name) {
        var type = new Type(name, name, "no.novari.fint.model.utdanning.elev", false, "hovedklasse");
        type.attributes.add(attribute(type, "systemId", "Identifikator", false, false));
        return type;
    }
    private static Attribute attribute(Type owner, String name, String type, boolean list, boolean optional) {
        return new Attribute(name, type, list, optional, owner, null);
    }
    private static Map<String, String> generate(Type type) { return new GraphqlGenerator().generate(new Model(List.of(type)), ALL); }
}
