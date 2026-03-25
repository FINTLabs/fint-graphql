package no.fint.graphql.config;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class JwtRolePathPrefixExtractor {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CLIENT_ROLE_PREFIX = "FINT_Client_";

    public Set<String> extractAllowedPathPrefixes(String authorization) {
        String token = getToken(authorization);
        if (StringUtils.isBlank(token)) {
            return Collections.emptySet();
        }

        try {
            JWTClaimsSet claimsSet = SignedJWT.parse(token).getJWTClaimsSet();
            Object rolesClaim = claimsSet != null ? getRolesClaim(claimsSet) : null;
            if (rolesClaim == null) {
                return Collections.emptySet();
            }

            Set<String> prefixes = collectPrefixes(rolesClaim);
            return prefixes.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(prefixes);
        } catch (Exception ex) {
            log.warn("Unable to parse JWT roles claim", ex);
            return Collections.emptySet();
        }
    }

    private Set<String> collectPrefixes(Object rolesClaim) {
        LinkedHashSet<String> prefixes = new LinkedHashSet<>();
        if (rolesClaim instanceof List<?> rolesList) {
            rolesList.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .forEach(role -> addPrefix(role, prefixes));
            return prefixes;
        }
        if (rolesClaim instanceof String rolesValue) {
            Arrays.stream(rolesValue.split("[,\\s]+"))
                    .forEach(role -> addPrefix(role, prefixes));
        }
        return prefixes;
    }

    private void addPrefix(String role, Set<String> prefixes) {
        String prefix = toPathPrefix(role);
        if (prefix != null) {
            prefixes.add(prefix);
        }
    }

    private String toPathPrefix(String role) {
        if (!StringUtils.startsWithIgnoreCase(role, CLIENT_ROLE_PREFIX)) {
            return null;
        }

        String suffix = role.substring(CLIENT_ROLE_PREFIX.length());
        if (StringUtils.isBlank(suffix)) {
            return null;
        }

        String path = tokenizeSuffix(suffix)
                .map(part -> part.toLowerCase(Locale.ROOT))
                .collect(Collectors.joining("/"));
        if (StringUtils.isBlank(path)) {
            return null;
        }

        return "/" + path + "/";
    }

    private Object getRolesClaim(JWTClaimsSet claimsSet) {
        Object rolesClaim = claimsSet.getClaim("roles");
        if (rolesClaim != null) {
            return rolesClaim;
        }
        return claimsSet.getClaim("Roles");
    }

    private Stream<String> tokenizeSuffix(String suffix) {
        if (suffix.contains("_")) {
            return Arrays.stream(suffix.split("_+"))
                    .map(StringUtils::trimToNull)
                    .filter(Objects::nonNull);
        }
        return Arrays.stream(suffix.split("(?<=[a-z])(?=[A-Z])"))
                .map(StringUtils::trimToNull)
                .filter(Objects::nonNull);
    }

    private String getToken(String authorization) {
        if (StringUtils.isBlank(authorization) || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return authorization.substring(BEARER_PREFIX.length()).trim();
    }
}
