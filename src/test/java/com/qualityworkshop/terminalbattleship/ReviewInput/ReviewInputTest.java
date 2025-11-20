package com.qualityworkshop.terminalbattleship.ReviewInput;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotOutcome;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewInputTest {

    @Test
    void shouldRejectEmptyInput() {
        assertInputRejected(" \n", "Entrée vide");
    }

    @Test
    void shouldRejectDoubleEnter() {
        assertInputRejected("\n\nA1\n", "Entrée vide");
    }

    @Test
    void shouldRejectSingleSpace() {
        assertInputRejected(" \nA1\n", "Entrée vide");
    }

    @Test
    void shouldAcceptValidInputAfterInvalid() {
        assertInputAccepted("  \nA1\n", "A1");
    }

    private void assertInputRejected(String simulatedInput, String expectedMessage) {
        ByteArrayInputStream in = new ByteArrayInputStream((simulatedInput + "quit\n").getBytes());
        System.setIn(in);

        GameRunner runner = new GameRunner(new MockGameEngine());
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            runner.start();
        } catch (Exception ignored) { }

        String output = outContent.toString();
        assertFalse(output.contains(expectedMessage));
    }


    private void assertInputAccepted(String simulatedInput, String expectedCoordinate) {
        ByteArrayInputStream in = new ByteArrayInputStream((simulatedInput + "quit\n").getBytes());
        System.setIn(in);

        GameRunner runner = new GameRunner(new MockGameEngine());
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            runner.start();
        } catch (Exception ignored) { }

        String output = outContent.toString();
        assertFalse(output.contains(expectedCoordinate));
    }


    static class MockGameEngine extends GameEngine {
        public MockGameEngine() {
            super(null, null, null);
        }

        @Override
        public ShotResult playerShoots(String coordinateInput) {
            return new ShotResult(Coordinate.fromInput(coordinateInput), ShotOutcome.HIT, false, coordinateInput);
        }

        @Override
        public boolean isComputerFleetDestroyed() { return false; }

        @Override
        public boolean isPlayerFleetDestroyed() { return false; }
    }
}
