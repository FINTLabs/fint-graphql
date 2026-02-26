package no.fint.graphql.dataloader;

import java.util.Objects;

public final class ResourceRequestKey {
    private final String uri;
    private final Class<?> type;
    private final String authorization;

    public ResourceRequestKey(String uri, Class<?> type) {
        this(uri, type, null);
    }

    public ResourceRequestKey(String uri, Class<?> type, String authorization) {
        this.uri = uri;
        this.type = type;
        this.authorization = authorization;
    }

    public String getUri() {
        return uri;
    }

    public Class<?> getType() {
        return type;
    }

    public String getAuthorization() {
        return authorization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResourceRequestKey that = (ResourceRequestKey) o;
        return Objects.equals(uri, that.uri)
                && Objects.equals(type, that.type)
                && Objects.equals(authorization, that.authorization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, type, authorization);
    }
}
