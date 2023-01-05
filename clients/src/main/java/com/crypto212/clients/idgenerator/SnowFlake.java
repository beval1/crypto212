package com.crypto212.clients.idgenerator;

import java.util.concurrent.atomic.AtomicLong;

public class SnowFlake {
    private static final long EPOCH = 1420070400000L;

    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;
    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    private final long workerId;
    private final AtomicLong sequence;
    private long lastTimestamp;

    public SnowFlake(long workerId) {
        if (workerId < 0 || workerId >= WORKER_ID_MAX_VALUE) {
            throw new IllegalArgumentException(String.format("Worker ID must be between %d and %d", 0, WORKER_ID_MAX_VALUE - 1));
        }
        this.workerId = workerId;
        this.sequence = new AtomicLong(0L);
        this.lastTimestamp = -1L;
    }

    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException(String.format("Invalid system clock: %d, lastTimestamp: %d", currentTimestamp, lastTimestamp));
        }

        if (currentTimestamp == lastTimestamp) {
            sequence.set((sequence.get() + 1) & SEQUENCE_MASK);
            if (sequence.get() == 0) {
                currentTimestamp = waitUntilNextMilli(currentTimestamp);
            }
        } else {
            sequence.set(0);
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence.get();
    }

    private long waitUntilNextMilli(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }
}
