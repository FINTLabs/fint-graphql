package no.fint.graphql.generator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.HexFormat;

/** Internal Gradle entry point. No standalone CLI or distribution is provided. */
public final class ModelDownload {
    private ModelDownload() {}

    public static void main(String[] args) throws Exception {
        if (args.length != 4) throw new IllegalArgumentException("Expected Gradle model version, SHA-256, cache path and offline flag");
        String version = args[0];
        if (!version.matches("[0-9]+\\.[0-9]+\\.[0-9]+(?:[-.][A-Za-z0-9.-]+)?")) throw new IllegalArgumentException("Invalid apiVersion: " + version);
        var uri = URI.create("https://raw.githubusercontent.com/FINTLabs/fint-informasjonsmodell/v" + version + "/FINT-informasjonsmodell.xml");
        fetch(uri, args[1], Path.of(args[2]), Boolean.parseBoolean(args[3]));
    }

    static void fetch(URI source, String expectedHash, Path destination, boolean offline) throws Exception {
        if (!expectedHash.matches("[a-f0-9]{64}")) throw new IllegalArgumentException("modelSha256 must be a lowercase SHA-256 digest");
        if (Files.exists(destination)) {
            verify(Files.readAllBytes(destination), expectedHash);
            return;
        }
        if (offline) throw new IOException("No verified information model in offline cache: " + destination);
        byte[] bytes;
        try (var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build()) {
            var request = HttpRequest.newBuilder(source).timeout(Duration.ofSeconds(60)).GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() != 200) throw new IOException("Model download returned HTTP " + response.statusCode() + " from " + source);
            bytes = response.body();
        }
        verify(bytes, expectedHash);
        Files.createDirectories(destination.toAbsolutePath().getParent());
        Path temporary = Files.createTempFile(destination.toAbsolutePath().getParent(), "model-", ".tmp");
        try {
            Files.write(temporary, bytes);
            Files.move(temporary, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    static void verify(byte[] bytes, String expectedHash) throws Exception {
        String actual = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        if (!actual.equals(expectedHash)) throw new IOException("Model SHA-256 mismatch: expected " + expectedHash + ", got " + actual);
    }
}
