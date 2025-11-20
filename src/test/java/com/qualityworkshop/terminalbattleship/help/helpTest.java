package com.qualityworkshop.terminalbattleship.help;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.game.ShotOutcome;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;



public class helpTest {

    @Test
    void testHelpLowercase() {
        assertHelpDisplaysRules("help");
    }

    @Test
    void testHelpUppercase() {
        assertHelpDisplaysRules("HELP");
    }

    @Test
    void testHelpMixedCase() {
        assertHelpDisplaysRules("HelP");
    }

    @Test
    void testHelpWithTrailingSpaces() {
        assertHelpDisplaysRules("help   ");
    }

    private void assertHelpDisplaysRules(String input) {
        TestGameRunner runner = new TestGameRunner(new MockGameEngine());

        String simulatedInput = input + "\n"; // plus besoin de quit
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            runner.askForPlayerShot();
        } catch (RuntimeException ignored) {

        }

        String output = outContent.toString();
        assertTrue(output.contains("Règles du jeu"),
                "La commande help doit afficher les règles pour: " + input);
    }



    static class MockGameEngine extends com.qualityworkshop.terminalbattleship.game.GameEngine {
        public MockGameEngine() {
            super(null, null, null); // Pas besoin de boards réels pour le test
        }

        @Override
        public boolean isComputerFleetDestroyed() { return true; }

        @Override
        public boolean isPlayerFleetDestroyed() { return false; }

        @Override
        public ShotResult playerShoots(String coordinateInput) {
            return new ShotResult(
                    Coordinate.fromInput(coordinateInput),
                    ShotOutcome.MISS,
                    true,
                    coordinateInput
            );
        }
    }
}
