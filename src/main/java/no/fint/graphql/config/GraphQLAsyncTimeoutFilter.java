package no.fint.graphql.config;

import graphql.ExecutionResultImpl;
import graphql.kickstart.execution.GraphQLObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GraphQLAsyncTimeoutFilter extends OncePerRequestFilter {

    private static final String TIMEOUT_WRITTEN_ATTR = GraphQLAsyncTimeoutFilter.class.getName() + ".timeoutWritten";

    private final GraphQLObjectMapper graphQLObjectMapper;

    @Value("${fint.graphql.async-request-timeout:PT2M}")
    private Duration asyncTimeout;

    @Value("${fint.graphql.query-timeout:PT1M50S}")
    private Duration queryTimeout;

    public GraphQLAsyncTimeoutFilter(GraphQLObjectMapper graphQLObjectMapper) {
        this.graphQLObjectMapper = graphQLObjectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        AtomicBoolean timedOut = new AtomicBoolean(false);
        AtomicBoolean writingTimeout = new AtomicBoolean(false);

        HttpServletResponse wrappedResponse = new HttpServletResponseWrapper(response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                if (timedOut.get() && !writingTimeout.get()) {
                    return new NoOpServletOutputStream();
                }
                return super.getOutputStream();
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                if (timedOut.get() && !writingTimeout.get()) {
                    return new PrintWriter(new NoOpWriter());
                }
                return super.getWriter();
            }
        };

        HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public javax.servlet.AsyncContext startAsync() throws IllegalStateException {
                javax.servlet.AsyncContext ctx = super.startAsync(this, wrappedResponse);
                configureAsyncContext(ctx);
                return ctx;
            }

            @Override
            public javax.servlet.AsyncContext startAsync(javax.servlet.ServletRequest req, javax.servlet.ServletResponse res)
                    throws IllegalStateException {
                javax.servlet.AsyncContext ctx = super.startAsync(req, wrappedResponse);
                configureAsyncContext(ctx);
                return ctx;
            }

            private void configureAsyncContext(javax.servlet.AsyncContext asyncContext) {
                long asyncTimeoutMs = getAsyncTimeoutMillis();
                if (asyncTimeoutMs > 0L) {
                    asyncContext.setTimeout(asyncTimeoutMs);
                }
                AtomicBoolean written = new AtomicBoolean(false);
                asyncContext.getRequest().setAttribute(TIMEOUT_WRITTEN_ATTR, written);
                asyncContext.addListener(new javax.servlet.AsyncListener() {
                    @Override
                    public void onTimeout(javax.servlet.AsyncEvent event) throws IOException {
                        HttpServletResponse asyncResponse = (HttpServletResponse) event.getSuppliedResponse();
                        AtomicBoolean flag = (AtomicBoolean) event.getSuppliedRequest().getAttribute(TIMEOUT_WRITTEN_ATTR);
                        if (flag == null) {
                            flag = written;
                        }
                        if (asyncResponse.isCommitted() || !flag.compareAndSet(false, true)) {
                            return;
                        }
                        writingTimeout.set(true);
                        timedOut.set(true);
                        if (!asyncResponse.isCommitted()) {
                            asyncResponse.resetBuffer();
                        }
                        long timeoutMs = queryTimeout != null ? queryTimeout.toMillis() : 0L;
                        ExecutionResultImpl result = new ExecutionResultImpl(
                                null,
                                Collections.singletonList(new QueryTimeoutGraphQLError(timeoutMs))
                        );
                        String json = graphQLObjectMapper.serializeResultAsJson(result);
                        asyncResponse.setStatus(HttpServletResponse.SC_OK);
                        asyncResponse.setContentType("application/json");
                        asyncResponse.setCharacterEncoding("UTF-8");
                        asyncResponse.getWriter().write(json);
                        asyncResponse.getWriter().flush();
                        writingTimeout.set(false);
                        event.getAsyncContext().complete();
                    }

                    @Override
                    public void onComplete(javax.servlet.AsyncEvent event) {
                    }

                    @Override
                    public void onError(javax.servlet.AsyncEvent event) {
                    }

                    @Override
                    public void onStartAsync(javax.servlet.AsyncEvent event) {
                    }
                });
            }
        };
        filterChain.doFilter(wrappedRequest, wrappedResponse);
    }

    private long getAsyncTimeoutMillis() {
        return asyncTimeout != null ? asyncTimeout.toMillis() : 0L;
    }

    private static final class NoOpWriter extends java.io.Writer {
        @Override
        public void write(char[] cbuf, int off, int len) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    }

    private static final class NoOpServletOutputStream extends ServletOutputStream {
        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(javax.servlet.WriteListener writeListener) {
        }

        @Override
        public void write(int b) {
        }
    }
}
