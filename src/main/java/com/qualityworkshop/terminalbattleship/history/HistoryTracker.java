package com.qualityworkshop.terminalbattleship.history;

import java.util.LinkedList;
import java.util.List;

public class HistoryTracker {

    private final LinkedList<String> history = new LinkedList<>();
    private final int maxSize;

    public HistoryTracker(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(String entry) {
        if (history.size() == maxSize) {
            history.removeFirst();
        }
        history.add(entry);
    }

    public List<String> getHistory() {
        return List.copyOf(history);
    }
}
