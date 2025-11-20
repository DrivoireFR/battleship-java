package com.qualityworkshop.terminalbattleship.timer;

public class Timer {

    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public String displayElapsedTime() {
        long elapsedMs = endTime - startTime;
        double elapsedSeconds = elapsedMs / 1000.0;
        return String.format("Réponse en %.2f secondes", elapsedSeconds);
    }
}
