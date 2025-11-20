package com.qualityworkshop.terminalbattleship.timer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TimerTest {

    @Test
    void testElapsedTimeLessThanOneSecond() throws InterruptedException {
        Timer timer = new Timer();
        timer.start();
        Thread.sleep(500); // 0.5 seconde
        timer.stop();
        String message = timer.displayElapsedTime();
        assertEquals("Délai très court (<1 seconde)", message);

    }

    @Test
    void testElapsedTimeNormal() throws InterruptedException {
        Timer timer = new Timer();
        timer.start();
        Thread.sleep(1500); // 1.5 seconde
        timer.stop();
        String message = timer.displayElapsedTime();
        assert(message.startsWith("Réponse en"));
    }

    @Test
    void testElapsedTimeOverSixtySeconds() {
        Timer timer = new Timer();
        timer.start();
        timer.stop();
        timer.startTime = System.currentTimeMillis() - 61_000;
        timer.endTime = System.currentTimeMillis();
        String message = timer.displayElapsedTime();
        assert(message.startsWith("Délai trop long"));
    }
}
