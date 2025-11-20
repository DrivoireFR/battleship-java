package com.qualityworkshop.terminalbattleship.game;

import com.qualityworkshop.terminalbattleship.board.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShotHistory {

    private final List<ShotRecord> history;

    public ShotHistory() {
        this.history = new ArrayList<>();
    }

    public void record(String shooter, Coordinate coordinate, ShotOutcome outcome) {
        if (history.size() >= 3) {
            history.remove(0);
        }
        history.add(new ShotRecord(shooter, coordinate, outcome));
    }

    public List<ShotRecord> getLastThree() {
        return Collections.unmodifiableList(history);
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public record ShotRecord(String shooter, Coordinate coordinate, ShotOutcome outcome) {
        @Override
        public String toString() {
            String outcomeText = switch (outcome) {
                case HIT -> "TouChé";
                case MISS -> "Raté";
                case ALL_SUNK -> "Coulé";
                case ALREADY_TARGETED -> "Déjà visé";
                case INVALID -> "Invalide";
            };
            return String.format("%s en %s -> %s", shooter, coordinate, outcomeText);
        }
    }
}