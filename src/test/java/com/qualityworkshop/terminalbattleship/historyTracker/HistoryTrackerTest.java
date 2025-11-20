package com.qualityworkshop.terminalbattleship.historyTracker;

import com.qualityworkshop.terminalbattleship.history.HistoryTracker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class HistoryTrackerTest {

    @Test
    void shouldStoreUpToThreeEntries() {
        HistoryTracker tracker = new HistoryTracker(3);
        tracker.add("A1 -> HIT");
        tracker.add("B2 -> MISS");
        tracker.add("C3 -> HIT");

        assertIterableEquals(
                java.util.List.of("A1 -> HIT", "B2 -> MISS", "C3 -> HIT"),
                tracker.getHistory()
        );

        tracker.add("D4 -> MISS");
        assertIterableEquals(
                java.util.List.of("B2 -> MISS", "C3 -> HIT", "D4 -> MISS"),
                tracker.getHistory()
        );
    }

    @Test
    void shouldHandleLessThanThreeEntries() {
        HistoryTracker tracker = new HistoryTracker(3);
        tracker.add("A1 -> HIT");

        assertIterableEquals(
                java.util.List.of("A1 -> HIT"),
                tracker.getHistory()
        );
    }

    @Test
    void shouldAllowRepeatedCoordinates() {
        HistoryTracker tracker = new HistoryTracker(3);
        tracker.add("A1 -> HIT");
        tracker.add("A1 -> MISS");
        tracker.add("A1 -> HIT");

        assertIterableEquals(
                java.util.List.of("A1 -> HIT", "A1 -> MISS", "A1 -> HIT"),
                tracker.getHistory()
        );
    }

    @Test
    void shouldMaintainOrder() {
        HistoryTracker tracker = new HistoryTracker(3);
        tracker.add("A1 -> HIT");
        tracker.add("B2 -> MISS");
        tracker.add("C3 -> HIT");
        tracker.add("D4 -> MISS");

        assertIterableEquals(
                java.util.List.of("B2 -> MISS", "C3 -> HIT", "D4 -> MISS"),
                tracker.getHistory()
        );
    }
}
