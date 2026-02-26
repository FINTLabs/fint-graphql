package no.fint.graphql;

public class WebClientRequestException extends RuntimeException {
    private final String uri;
    private final String queryId;
    private final String requestId;

    public WebClientRequestException(String message, Throwable cause, String uri, String queryId, String requestId) {
        super(message, cause);
        this.uri = uri;
        this.queryId = queryId;
        this.requestId = requestId;
    }

    public String getUri() {
        return uri;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getRequestId() {
        return requestId;
    }
}
