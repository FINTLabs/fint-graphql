package no.fint.graphql.config;

import graphql.ExecutionResultImpl;
import graphql.kickstart.execution.GraphQLObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class GraphQLAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final GraphQLObjectMapper graphQLObjectMapper;
    private final BearerTokenAuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    public GraphQLAuthenticationEntryPoint(GraphQLObjectMapper graphQLObjectMapper) {
        this.graphQLObjectMapper = graphQLObjectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        if (!isGraphQLRequest(request)) {
            delegate.commence(request, response, authException);
            return;
        }
        writeUnauthorizedResponse(response);
    }

    public void writeUnauthorizedResponse(HttpServletResponse response) throws IOException {
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
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    private boolean isGraphQLRequest(HttpServletRequest request) {
        if (request == null || !"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String uri = request.getRequestURI();
        return uri != null && uri.endsWith("/graphql");
    }
}
