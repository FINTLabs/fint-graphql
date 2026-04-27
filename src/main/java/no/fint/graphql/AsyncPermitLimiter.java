package no.fint.graphql;

import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicBoolean;

final class AsyncPermitLimiter {

    private final Deque<PendingAcquire> waiters = new ArrayDeque<>();
    private final int maxPermits;
    private int availablePermits;

    AsyncPermitLimiter(int maxPermits) {
        this.maxPermits = Math.max(1, maxPermits);
        this.availablePermits = this.maxPermits;
    }

    Mono<Permit> acquire() {
        return Mono.create(sink -> {
            PendingAcquire pendingAcquire = new PendingAcquire(sink);
            boolean acquiredImmediately = false;
            synchronized (this) {
                if (availablePermits > 0) {
                    availablePermits--;
                    acquiredImmediately = true;
                } else {
                    waiters.addLast(pendingAcquire);
                }
            }
            if (acquiredImmediately) {
                pendingAcquire.completeSuccess(new Permit(this));
                return;
            }
            sink.onCancel(() -> cancelPendingAcquire(pendingAcquire));
        });
    }

    private void cancelPendingAcquire(PendingAcquire pendingAcquire) {
        if (!pendingAcquire.markTerminal()) {
            return;
        }
        synchronized (this) {
            waiters.remove(pendingAcquire);
        }
    }

    private void release() {
        PendingAcquire next = null;
        synchronized (this) {
            while (!waiters.isEmpty()) {
                PendingAcquire candidate = waiters.pollFirst();
                if (candidate.markTerminal()) {
                    next = candidate;
                    break;
                }
            }
            if (next == null) {
                if (availablePermits < maxPermits) {
                    availablePermits++;
                }
                return;
            }
        }
        next.completeSuccess(new Permit(this));
    }

    synchronized int getAvailablePermits() {
        return availablePermits;
    }

    synchronized int getQueueLength() {
        return waiters.size();
    }

    static final class Permit {
        private final AsyncPermitLimiter limiter;
        private final AtomicBoolean released = new AtomicBoolean();

        private Permit(AsyncPermitLimiter limiter) {
            this.limiter = limiter;
        }

        Mono<Void> release() {
            if (released.compareAndSet(false, true)) {
                limiter.release();
            }
            return Mono.empty();
        }
    }

    private static final class PendingAcquire {
        private final MonoSink<Permit> sink;
        private final AtomicBoolean terminal = new AtomicBoolean();

        private PendingAcquire(MonoSink<Permit> sink) {
            this.sink = sink;
        }

        private boolean markTerminal() {
            return terminal.compareAndSet(false, true);
        }

        private void completeSuccess(Permit permit) {
            sink.success(permit);
        }
    }
}
