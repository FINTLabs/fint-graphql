package no.fint.graphql.config;

import graphql.ExecutionResultImpl;
import graphql.kickstart.execution.GraphQLObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.BlacklistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Guards GraphQL requests by requiring a valid Authorization header and rejecting
 * blacklisted client IPs with a 401 GraphQL error response.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GraphQLAuthFilter extends OncePerRequestFilter {

    private final GraphQLObjectMapper graphQLObjectMapper;
    private final BlacklistService blacklistService;

    public GraphQLAuthFilter(GraphQLObjectMapper graphQLObjectMapper, BlacklistService blacklistService) {
        this.graphQLObjectMapper = graphQLObjectMapper;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isGraphQLRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (blacklistService.isBlacklisted(getRemoteIp(request))) {
            log.warn("Blacklisted request from {}", getRemoteIp(request));
            writeUnauthorizedResponse(response);
            return;
        } else if (!hasValidJwt(getAuthorization(request))) {
            log.warn("Request without JWT token from {}", getRemoteIp(request));
            writeUnauthorizedResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isGraphQLRequest(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String uri = request.getRequestURI();
        return uri != null && uri.endsWith("/graphql");
    }

    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private boolean hasValidJwt(String authorization) {
        if (StringUtils.isBlank(authorization)) {
            return false;
        }
        if (!authorization.startsWith("Bearer ")) {
            return false;
        }
        String token = authorization.substring("Bearer ".length()).trim();
        if (token.isEmpty()) {
            return false;
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }
        for (String part : parts) {
            if (StringUtils.isBlank(part)) {
                return false;
            }
        }
        return true;
    }

    private String getRemoteIp(HttpServletRequest request) {
        if (request == null) return null;
        return request.getRemoteAddr();
    }

    private void writeUnauthorizedResponse(HttpServletResponse response) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        ExecutionResultImpl result = new ExecutionResultImpl(
                null,
                Collections.singletonList(new AuthGraphQLError("Unauthorized"))
        );
        String json = graphQLObjectMapper.serializeResultAsJson(result);
        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
