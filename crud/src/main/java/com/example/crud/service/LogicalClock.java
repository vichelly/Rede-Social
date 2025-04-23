package com.example.crud.service;

import java.util.concurrent.atomic.AtomicInteger;

public class LogicalClock {
    private static final AtomicInteger clock = new AtomicInteger(0);

    public static synchronized int tick() {
        return clock.incrementAndGet();
    }

    public static synchronized int update(int receivedTimestamp) {
        int current = clock.get();
        clock.set(Math.max(current, receivedTimestamp) + 1);
        return clock.get();
    }

    public static int get() {
        return clock.get();
    }
}
