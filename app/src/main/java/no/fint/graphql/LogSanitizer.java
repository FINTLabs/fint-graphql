package no.fint.graphql;

public final class LogSanitizer {
    private LogSanitizer() {
    }

    public static String singleLine(String value) {
        return value == null ? null : value.replace('\r', '_').replace('\n', '_')
                .replace('\u0085', '_').replace('\u2028', '_').replace('\u2029', '_');
    }
}
