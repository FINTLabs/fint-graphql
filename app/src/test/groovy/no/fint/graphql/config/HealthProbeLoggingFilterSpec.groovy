package no.fint.graphql.config

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import jakarta.servlet.FilterChain
import no.fint.graphql.WebClientRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

import java.time.Duration
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class HealthProbeLoggingFilterSpec extends Specification {
    Logger logger = (Logger) LoggerFactory.getLogger(HealthProbeLoggingFilter)
    def appender = new QueueAppender()
    HealthProbeLoggingFilter filter
    def request = new MockHttpServletRequest('GET', '/actuator/health/\r\nFORGED\u0085\u2028\u2029')
    def response = new MockHttpServletResponse()

    def setup() {
        appender.start()
        logger.addAppender(appender)
        def indicators = Stub(ObjectProvider) {
            getIfAvailable(_) >> [:]
        }
        filter = new HealthProbeLoggingFilter(indicators, Stub(WebClientRequest))
        ReflectionTestUtils.setField(filter, 'slowLogThreshold', Duration.ZERO)
        request.remoteAddr = '127.0.0.1\r\nFORGED'
    }

    def cleanup() {
        filter.destroy()
        logger.detachAppender(appender)
        appender.stop()
    }

    def "failed probe logs cannot contain forged request lines"() {
        when:
        filter.doFilter(request, response, { req, res -> res.status = 503 } as FilterChain)

        then:
        safe(appender.events.poll(2, TimeUnit.SECONDS))
        request.requestURI == '/actuator/health/\r\nFORGED\u0085\u2028\u2029'
    }

    def "probe exceptions retain their cause and sanitize request values"() {
        given:
        def failure = new IOException('Probe failed')

        when:
        filter.doFilter(request, response, { req, res -> throw failure } as FilterChain)

        then:
        def thrown = thrown(IOException)
        thrown.is(failure)
        def event = appender.events.poll(2, TimeUnit.SECONDS)
        safe(event)
        event.throwableProxy.message == 'Probe failed'
        safe(appender.events.poll(2, TimeUnit.SECONDS))
    }

    def "scheduled slow probe and completion logs sanitize captured request values"() {
        given:
        ReflectionTestUtils.setField(filter, 'slowLogThreshold', Duration.ofMillis(5))
        ILoggingEvent slowEvent

        when:
        filter.doFilter(request, response, { req, res ->
            slowEvent = appender.events.poll(2, TimeUnit.SECONDS)
        } as FilterChain)

        then:
        safe(slowEvent)
        slowEvent.formattedMessage.startsWith('Actuator health probe still running')
        def completed = appender.events.poll(2, TimeUnit.SECONDS)
        safe(completed)
        completed.formattedMessage.startsWith('Actuator health probe completed')
    }

    private static boolean safe(ILoggingEvent event) {
        assert event != null
        assert event.formattedMessage.contains('path=/actuator/health/__FORGED___')
        assert event.formattedMessage.contains('remote=127.0.0.1__FORGED')
        assert !(event.formattedMessage =~ /[\r\n\u0085\u2028\u2029]/)
        true
    }

    private static class QueueAppender extends AppenderBase<ILoggingEvent> {
        final events = new LinkedBlockingQueue<ILoggingEvent>()

        @Override
        protected void append(ILoggingEvent event) {
            event.prepareForDeferredProcessing()
            events.add(event)
        }
    }
}
