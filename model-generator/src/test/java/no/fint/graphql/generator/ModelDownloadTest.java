package no.fint.graphql.generator;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ModelDownloadTest {
    @TempDir Path temporary;

    @Test
    void downloadsOnceVerifiesCacheEvenOfflineAndNeverCachesBadResponses() throws Exception {
        byte[] data = "verified model".getBytes(StandardCharsets.UTF_8);
        var requests = new AtomicInteger();
        var server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/model", exchange -> {
            requests.incrementAndGet();
            exchange.sendResponseHeaders(200, data.length);
            try (var body = exchange.getResponseBody()) { body.write(data); }
        });
        server.createContext("/missing", exchange -> {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        });
        server.start();
        try {
            var uri = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/model");
            var cache = temporary.resolve("cache/model.xml");
            String hash = hash(data);
            assertThrows(Exception.class, () -> ModelDownload.fetch(uri, hash, cache, true));
            assertEquals(0, requests.get());
            ModelDownload.fetch(uri, hash, cache, false);
            assertArrayEquals(data, Files.readAllBytes(cache));
            ModelDownload.fetch(uri, hash, cache, true);
            ModelDownload.fetch(uri, hash, cache, false);
            assertEquals(1, requests.get());
            Files.writeString(cache, "corrupt");
            assertTrue(assertThrows(Exception.class, () -> ModelDownload.fetch(uri, hash, cache, true)).getMessage().contains("SHA-256 mismatch"));
            assertThrows(Exception.class, () -> ModelDownload.fetch(uri, hash, cache, false));
            assertEquals(1, requests.get());
            var wrong = temporary.resolve("wrong/model.xml");
            assertThrows(Exception.class, () -> ModelDownload.fetch(uri, "0".repeat(64), wrong, false));
            assertFalse(Files.exists(wrong));
            assertThrows(Exception.class, () -> ModelDownload.fetch(uri.resolve("/missing"), hash, wrong, false));
            assertFalse(Files.exists(wrong));
        } finally {
            server.stop(0);
        }
    }

    static String hash(byte[] bytes) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    }
}
