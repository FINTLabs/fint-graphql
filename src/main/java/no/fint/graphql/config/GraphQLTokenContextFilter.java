package no.fint.graphql.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.BlacklistService;
import no.fint.graphql.GraphQLRequestAttributes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class GraphQLTokenContextFilter extends OncePerRequestFilter {

    private final BlacklistService blacklistService;
    private final JwtRolePathPrefixExtractor jwtRolePathPrefixExtractor;
    private final GraphQLAuthenticationEntryPoint authenticationEntryPoint;

    public GraphQLTokenContextFilter(
            BlacklistService blacklistService,
            JwtRolePathPrefixExtractor jwtRolePathPrefixExtractor,
            GraphQLAuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.blacklistService = blacklistService;
        this.jwtRolePathPrefixExtractor = jwtRolePathPrefixExtractor;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isGraphQLRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (blacklistService.isBlacklisted(request.getRemoteAddr())) {
            log.warn("Blacklisted request from {}", request.getRemoteAddr());
            authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("Unauthorized"));
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        Jwt jwt = jwtAuthenticationToken.getToken();
        String organisationId = extractOrganisationId(jwt);
        if (StringUtils.isBlank(organisationId)) {
            log.warn("Request without fintAssetIDs claim from {}", request.getRemoteAddr());
            authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("Unauthorized"));
            return;
        }

        GraphQLRequestAttributes.setAllowedPathPrefixes(
                request,
                jwtRolePathPrefixExtractor.extractAllowedPathPrefixes(jwt)
        );
        GraphQLRequestAttributes.setOrganisationId(request, organisationId);
        filterChain.doFilter(request, response);
    }

    private String extractOrganisationId(Jwt jwt) {
        if (jwt == null) {
            return null;
        }
        Object claim = jwt.getClaim("fintAssetIDs");
        return claim instanceof String ? StringUtils.trimToNull((String) claim) : null;
    }

    private boolean isGraphQLRequest(HttpServletRequest request) {
        if (request == null || !"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String uri = request.getRequestURI();
        return uri != null && uri.endsWith("/graphql");
    }
}
