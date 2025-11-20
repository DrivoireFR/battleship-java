package com.qualityworkshop.terminalbattleship.timer;

public class Timer {

    long startTime;
    long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public String displayElapsedTime() {
        long elapsedMs = endTime - startTime;
        double elapsedSeconds = elapsedMs / 1000.0;

        if (elapsedSeconds < 1) {
            return "Délai très court (<1 seconde)";
        } else if (elapsedSeconds > 60) {
            return String.format("Délai trop long (%.2f secondes)", elapsedSeconds);
        } else {
            return String.format("Réponse en %.2f secondes", elapsedSeconds);
        }
    }
}
