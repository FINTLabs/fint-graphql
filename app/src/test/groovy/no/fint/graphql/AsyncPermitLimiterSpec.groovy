package no.fint.graphql

import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Duration

class AsyncPermitLimiterSpec extends Specification {
    def "cancelling before requesting a granted permit returns it to the limiter"() {
        given:
        def limiter = new AsyncPermitLimiter(1)

        when:
        reactor.test.StepVerifier.create(limiter.acquire(), 0).thenCancel().verify()

        then:
        limiter.availablePermits == 1
        limiter.queueLength == 0
    }

    def "cancelled queued acquisition does not consume the next released permit"() {
        given:
        def limiter = new AsyncPermitLimiter(1)
        def first = limiter.acquire().block()
        def queued = limiter.acquire().subscribe()

        expect:
        limiter.availablePermits == 0
        limiter.queueLength == 1

        when:
        queued.dispose()
        first.release().block()
        def next = limiter.acquire().block(Duration.ofSeconds(1))

        then:
        limiter.queueLength == 0
        next != null

        cleanup:
        next?.release()?.block()
    }

    def "release is idempotent and transfers a permit to a waiting subscriber"() {
        given:
        def limiter = new AsyncPermitLimiter(1)
        def first = limiter.acquire().block()
        def waiting = limiter.acquire().toFuture()

        when:
        first.release().block()
        first.release().block()
        def second = waiting.get()

        then:
        limiter.availablePermits == 0
        limiter.queueLength == 0

        when:
        second.release().block()
        second.release().block()

        then:
        limiter.availablePermits == 1
    }

    def "cancelling active work releases its permit"() {
        given:
        def limiter = new AsyncPermitLimiter(1)
        def work = Mono.usingWhen(limiter.acquire(), { Mono.never() },
                { it.release() }, { permit, error -> permit.release() }, { it.release() }).subscribe()

        expect:
        limiter.availablePermits == 0

        when:
        work.dispose()

        then:
        limiter.availablePermits == 1
        limiter.queueLength == 0
    }
}
