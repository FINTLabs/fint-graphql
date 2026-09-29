package no.fint.graphql.generator;

import com.sun.source.util.JavacTask;
import graphql.language.AstPrinter;
import graphql.parser.Parser;
import org.junit.jupiter.api.Test;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

class GenerationParityTest {
    private static final String PERSON_SERVICE = "model/model/person/PersonService.java";

    @Test
    void everyLegacyFileAndThePublishedApplicationContractArePreserved() throws Exception {
        // Explicitly updating these references is part of a future model version upgrade.
        assertEquals("4.1.0", System.getProperty("fint.model.version"));
        assertEquals("ff2e292de2ccb6fe9c334fe46ae1cb3b1eae2b919545345731b2dd3247b9b826", System.getProperty("fint.model.sha256"));
        var model = new XmiParser().parse(Path.of(System.getProperty("fint.model.file")));
        var generator = new GraphqlGenerator();
        var legacyOptions = new GraphqlGenerator.Options(Set.of("Fravar", "Fravarstype"), Set.of(), Set.of());
        var legacy = baseline("legacy");
        var generated = generator.generate(model, legacyOptions);
        assertEquals(394, legacy.size());
        assertSourcesEqual(legacy, generated);

        var application = new TreeMap<>(generator.generate(model, GraphqlGenerator.Options.application()));
        assertFalse(application.containsKey(PERSON_SERVICE), "PersonService must only have one hand-maintained implementation");
        var person = Path.of(System.getProperty("fint.app.directory"), "src/main/java/no/fint/graphql", PERSON_SERVICE);
        application.put(PERSON_SERVICE, Files.readString(person));
        assertSourcesEqual(baseline("application"), application);
        assertEquals(generated, generator.generate(model, legacyOptions), "Rendering must not mutate the input model");
    }

    private static void assertSourcesEqual(Map<String, String> expected, Map<String, String> actual) throws Exception {
        assertEquals(expected.keySet(), actual.keySet(), "Generated file inventory");
        var expectedJava = javaSyntax(expected);
        var actualJava = javaSyntax(actual);
        for (var name : expected.keySet()) {
            if (name.endsWith(".java")) assertEquals(expectedJava.get(name), actualJava.get(name), name);
            else assertEquals(AstPrinter.printAstCompact(new Parser().parseDocument(expected.get(name))),
                    AstPrinter.printAstCompact(new Parser().parseDocument(actual.get(name))), name);
        }
    }

    // Compare syntax trees: whitespace and comments may change, string literals and behavior may not.
    private static Map<String, String> javaSyntax(Map<String, String> files) throws Exception {
        var compiler = ToolProvider.getSystemJavaCompiler();
        var diagnostics = new DiagnosticCollector<JavaFileObject>();
        var sources = files.entrySet().stream().filter(e -> e.getKey().endsWith(".java"))
                .map(e -> new SimpleJavaFileObject(URI.create("string:///" + e.getKey()), JavaFileObject.Kind.SOURCE) {
                    @Override public CharSequence getCharContent(boolean ignoreEncodingErrors) { return e.getValue(); }
                }).toList();
        var result = new TreeMap<String, String>();
        try (var manager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            var task = (JavacTask) compiler.getTask(null, manager, diagnostics, List.of("-proc:none"), null, sources);
            for (var tree : task.parse()) result.put(tree.getSourceFile().getName().substring(1), tree.toString());
        }
        assertTrue(diagnostics.getDiagnostics().stream().noneMatch(d -> d.getKind() == Diagnostic.Kind.ERROR), diagnostics.getDiagnostics().toString());
        return result;
    }

    private static Map<String, String> baseline(String name) throws Exception {
        var result = new LinkedHashMap<String, String>();
        try (var zip = new ZipInputStream(GenerationParityTest.class.getResourceAsStream("/baseline/" + name + ".zip"))) {
            for (var entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                result.put(entry.getName(), new String(zip.readAllBytes(), StandardCharsets.UTF_8));
            }
        }
        return result;
    }
}
