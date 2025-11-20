package com.qualityworkshop.terminalbattleship.timer;

import java.time.Duration;
import java.time.Instant;

public class Timer {

    private Instant startTime;
    private Instant endTime;

    public void start() {
        startTime = Instant.now();
        endTime = null;
    }

    public void stop() {
        if (startTime == null) {
            throw new IllegalStateException("Le timer n'a pas été démarré.");
        }
        endTime = Instant.now();
    }

    public long getElapsedSeconds() {
        if (startTime == null) {
            throw new IllegalStateException("Le timer n'a pas été démarré.");
        }
        Instant effectiveEnd = (endTime != null) ? endTime : Instant.now();
        return Duration.between(startTime, effectiveEnd).getSeconds();
    }

}
