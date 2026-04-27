package no.fint.graphql.config;

import lombok.extern.slf4j.Slf4j;
import no.fint.graphql.WebClientRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Slf4j
public class HealthProbeLoggingFilter extends OncePerRequestFilter {

    private final Map<String, HealthIndicator> healthIndicators;
    private final WebClientRequest webClientRequest;
    private final ScheduledExecutorService scheduler;

    @Value("${fint.health.probe.slow-log-threshold:PT4S}")
    private Duration slowLogThreshold;

    public HealthProbeLoggingFilter(ObjectProvider<Map<String, HealthIndicator>> healthIndicatorsProvider,
                                    WebClientRequest webClientRequest) {
        this.healthIndicators = healthIndicatorsProvider.getIfAvailable(Map::of);
        this.webClientRequest = webClientRequest;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "health-probe-diagnostics");
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path == null
                || !(path.endsWith("/actuator/health")
                || path.endsWith("/actuator/health/")
                || path.contains("/actuator/health/"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startNanos = System.nanoTime();
        AtomicBoolean completed = new AtomicBoolean(false);
        AtomicBoolean slowWarningLogged = new AtomicBoolean(false);
        ScheduledFuture<?> slowWarning = scheduleSlowWarning(request, startNanos, completed, slowWarningLogged);

        try {
            filterChain.doFilter(request, response);
        } catch (ServletException | IOException | RuntimeException ex) {
            completed.set(true);
            log.warn("Actuator health probe failed with exception path={} remote={} durationMs={} diagnostics={} indicators={}",
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    elapsedMs(startNanos),
                    diagnostics(),
                    describeHealthIndicators(),
                    ex);
            throw ex;
        } finally {
            completed.set(true);
            if (slowWarning != null) {
                slowWarning.cancel(false);
            }
            long durationMs = elapsedMs(startNanos);
            int status = response.getStatus();
            if (status >= 500 || durationMs >= slowThresholdMs()) {
                log.warn("Actuator health probe completed status={} path={} remote={} durationMs={} diagnostics={} indicators={}",
                        status,
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        durationMs,
                        diagnostics(),
                        describeHealthIndicators());
            } else if (slowWarningLogged.get()) {
                log.info("Actuator health probe recovered status={} path={} remote={} durationMs={}",
                        status,
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        durationMs);
            }
        }
    }

    private ScheduledFuture<?> scheduleSlowWarning(HttpServletRequest request,
                                                   long startNanos,
                                                   AtomicBoolean completed,
                                                   AtomicBoolean slowWarningLogged) {
        long thresholdMs = slowThresholdMs();
        if (thresholdMs <= 0L) {
            return null;
        }
        String path = request.getRequestURI();
        String remote = request.getRemoteAddr();
        return scheduler.schedule(() -> {
            if (completed.get()) {
                return;
            }
            slowWarningLogged.set(true);
            log.warn("Actuator health probe still running path={} remote={} durationMs={} diagnostics={}",
                    path,
                    remote,
                    elapsedMs(startNanos),
                    diagnostics());
        }, thresholdMs, TimeUnit.MILLISECONDS);
    }

    private long slowThresholdMs() {
        return slowLogThreshold != null ? slowLogThreshold.toMillis() : 0L;
    }

    private long elapsedMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }

    private String describeHealthIndicators() {
        if (healthIndicators.isEmpty()) {
            return "none";
        }
        return healthIndicators.entrySet().stream()
                .map(entry -> describeHealthIndicator(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("; "));
    }

    private String describeHealthIndicator(String name, HealthIndicator indicator) {
        try {
            Health health = indicator.health();
            return name + "=" + health.getStatus() + " details=" + health.getDetails();
        } catch (RuntimeException ex) {
            return name + "=ERROR exception=" + ex.getClass().getSimpleName() + " message=" + ex.getMessage();
        }
    }

    private String diagnostics() {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        MemoryUsage heap = memory.getHeapMemoryUsage();
        MemoryUsage nonHeap = memory.getNonHeapMemoryUsage();
        long[] deadlockedThreads = threads.findDeadlockedThreads();

        return "heapUsedMb=" + toMb(heap.getUsed())
                + " heapCommittedMb=" + toMb(heap.getCommitted())
                + " heapMaxMb=" + toMb(heap.getMax())
                + " nonHeapUsedMb=" + toMb(nonHeap.getUsed())
                + " threads=" + threads.getThreadCount()
                + " peakThreads=" + threads.getPeakThreadCount()
                + " deadlockedThreads=" + (deadlockedThreads == null ? 0 : deadlockedThreads.length)
                + " gc=" + garbageCollectors()
                + " webClientCacheSize=" + webClientRequest.getReferenceDataCacheEstimatedSize()
                + " webClientPermitsAvailable=" + webClientRequest.getAvailableConcurrencyPermits()
                + " webClientPermitWaiters=" + webClientRequest.getQueuedConcurrencyWaiters();
    }

    private String garbageCollectors() {
        return ManagementFactory.getGarbageCollectorMXBeans().stream()
                .map(this::garbageCollector)
                .collect(Collectors.joining(","));
    }

    private String garbageCollector(GarbageCollectorMXBean gc) {
        return gc.getName()
                + "{count=" + gc.getCollectionCount()
                + ",timeMs=" + gc.getCollectionTime()
                + "}";
    }

    private long toMb(long bytes) {
        if (bytes < 0L) {
            return -1L;
        }
        return bytes / (1024L * 1024L);
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdownNow();
    }
}
