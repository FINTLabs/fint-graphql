package no.fint.graphql.dataloader;

import java.util.Objects;

public final class ResourceRequestKey {
    private final String uri;
    private final Class<?> type;

    public ResourceRequestKey(String uri, Class<?> type) {
        this.uri = uri;
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public Class<?> getType() {
        return type;
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
        return Objects.equals(uri, that.uri) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, type);
    }
}
