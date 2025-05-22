package com.sd.demo.Clock;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class LogicalClock {
    private AtomicInteger time = new AtomicInteger(0);

    public int tick() {
        return time.incrementAndGet();
    }

    public void update(int receivedTime) {
        time.set(Math.max(time.get(), receivedTime) + 1);
    }

    public int getTime() {
        return time.get();
    }
}