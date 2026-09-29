package no.fint.graphql.generator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Build-only adapter; model parsing and rendering are independently testable Java APIs. */
public final class GenerateGraphql {
    private GenerateGraphql() {}

    public static void main(String[] args) throws Exception {
        if (args.length != 5) throw new IllegalArgumentException("Expected Gradle model, output, exclusions, schema exclusions and manual services");
        var options = new GraphqlGenerator.Options(values(args[2]), values(args[3]), values(args[4]));
        var output = new GraphqlGenerator().generate(new XmiParser().parse(Path.of(args[0])), options);
        write(output, Path.of(args[1]));
        System.out.println("Generated " + output.size() + " GraphQL source and schema files in " + args[1]);
    }

    private static Set<String> values(String value) {
        return Arrays.stream(value.split(",")).filter(s -> !s.isBlank()).collect(Collectors.toUnmodifiableSet());
    }

    static void write(Map<String, String> files, Path output) throws IOException {
        output = output.toAbsolutePath().normalize();
        Files.createDirectories(output.getParent());
        Path staging = Files.createTempDirectory(output.getParent(), "graphql-staging-");
        try {
            for (var entry : files.entrySet()) {
                String relative = entry.getKey();
                if (relative.startsWith("model/")) relative = "java/no/fint/graphql/" + relative;
                else if (relative.startsWith("schema/")) relative = "resources/" + relative;
                else throw new IOException("Unexpected generated path: " + relative);
                Path destination = staging.resolve(relative).normalize();
                if (!destination.startsWith(staging)) throw new IOException("Generated path escapes output: " + relative);
                Files.createDirectories(destination.getParent());
                Files.writeString(destination, entry.getValue(), StandardCharsets.UTF_8);
            }
            // Only replace owned build output after all parsing, validation and rendering succeeded.
            deleteTree(output);
            Files.move(staging, output, StandardCopyOption.ATOMIC_MOVE);
        } finally {
            deleteTree(staging);
        }
    }

    private static void deleteTree(Path directory) throws IOException {
        if (!Files.exists(directory)) return;
        try (var paths = Files.walk(directory)) {
            for (var path : paths.sorted(Comparator.reverseOrder()).toList()) Files.delete(path);
        }
    }
}
