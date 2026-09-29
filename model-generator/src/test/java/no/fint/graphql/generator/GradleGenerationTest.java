package no.fint.graphql.generator;

import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Exercises the real repository build scripts in an isolated checkout without recursive tests. */
class GradleGenerationTest {
    @TempDir Path directory;

    @Test
    void automaticGenerationIsIncrementalOfflineAndTracksModelAndGeneratorChanges() throws Exception {
        Path repository = Path.of(System.getProperty("fint.repository.directory"));
        for (var name : List.of("build.gradle", "settings.gradle", "gradle.properties", "app/build.gradle", "model-generator/build.gradle")) {
            copy(repository.resolve(name), directory.resolve(name));
        }
        Path source = repository.resolve("model-generator/src/main");
        try (var paths = Files.walk(source)) {
            for (var path : paths.filter(Files::isRegularFile).toList()) copy(path, directory.resolve("model-generator/src/main").resolve(source.relativize(path)));
        }
        String version = System.getProperty("fint.model.version");
        String hash = System.getProperty("fint.model.sha256");
        Path model = cachedModel(version, hash);
        copy(Path.of(System.getProperty("fint.model.file")), model);

        var first = runner(":app:generateGraphql").build();
        assertEquals(TaskOutcome.SUCCESS, first.task(":app:generateGraphql").getOutcome());
        var second = runner(":app:generateGraphql").build();
        assertEquals(TaskOutcome.UP_TO_DATE, second.task(":app:generateGraphql").getOutcome());
        assertEquals(TaskOutcome.SUCCESS, second.task(":model-generator:downloadFintModel").getOutcome(), "Cache must still be verified");
        Path output = directory.resolve("app/build/generated/graphql");
        assertTrue(Files.exists(output.resolve("resources/schema/root.graphqls")));
        assertFalse(Files.exists(output.resolve("java/no/fint/graphql/model/model/person/PersonService.java")));

        Path adapter = directory.resolve("model-generator/src/main/java/no/fint/graphql/generator/GenerateGraphql.java");
        Files.writeString(adapter, Files.readString(adapter).replace("Generated ", "Regenerated "));
        assertEquals(TaskOutcome.SUCCESS, runner(":app:generateGraphql").build().task(":app:generateGraphql").getOutcome(), "Generator classpath is a task input");

        // A different model byte stream and version must invalidate generation.
        byte[] changed = (Files.readString(model, java.nio.charset.Charset.forName("windows-1252")) + "\r\n").getBytes(java.nio.charset.Charset.forName("windows-1252"));
        String changedHash = ModelDownloadTest.hash(changed);
        Path nextModel = cachedModel("4.1.1", changedHash);
        Files.createDirectories(nextModel.getParent());
        Files.write(nextModel, changed);
        Path properties = directory.resolve("gradle.properties");
        Files.writeString(properties, Files.readString(properties).replace("apiVersion=" + version, "apiVersion=4.1.1").replace(hash, changedHash));
        assertEquals(TaskOutcome.SUCCESS, runner(":app:generateGraphql").build().task(":app:generateGraphql").getOutcome());

        Files.writeString(output.resolve("resources/schema/stale.graphqls"), "stale");
        runner(":app:generateGraphql", "--rerun-tasks").build();
        assertFalse(Files.exists(output.resolve("resources/schema/stale.graphqls")));
        runner("clean", ":app:generateGraphql").build();
        assertTrue(Files.exists(nextModel), "clean must retain the verified model cache");
        assertTrue(Files.exists(output.resolve("resources/schema/root.graphqls")));

        Files.writeString(nextModel, "corrupt");
        assertTrue(runner(":app:generateGraphql").buildAndFail().getOutput().contains("Model SHA-256 mismatch"));
        Files.delete(nextModel);
        assertTrue(runner(":app:generateGraphql").buildAndFail().getOutput().contains("No verified information model in offline cache"));
        String tasks = runner(":model-generator:tasks", "--all").build().getOutput();
        assertFalse(tasks.contains("publishToMavenLocal"));
        assertFalse(tasks.contains("publishAllPublications"));
        assertFalse(tasks.contains("installDist"));
        assertFalse(tasks.contains("bootJar"));
    }

    private GradleRunner runner(String... tasks) {
        var args = new ArrayList<>(List.of("--offline", "--stacktrace", "--max-workers=2",
                "-Dorg.gradle.jvmargs=-Xmx256m", "--gradle-user-home", System.getProperty("fint.gradle.user.home")));
        args.addAll(List.of(tasks));
        return GradleRunner.create().withProjectDir(directory.toFile())
                .withGradleInstallation(new File(System.getProperty("fint.gradle.home")))
                .withTestKitDir(directory.resolve("test-kit").toFile()).withArguments(args);
    }

    private Path cachedModel(String version, String hash) {
        return directory.resolve(".gradle/fint-model/" + version + "/" + hash + "/FINT-informasjonsmodell.xml");
    }
    private static void copy(Path from, Path to) throws Exception {
        Files.createDirectories(to.getParent());
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }
}
