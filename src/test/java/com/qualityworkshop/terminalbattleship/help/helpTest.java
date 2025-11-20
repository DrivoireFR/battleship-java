package com.qualityworkshop.terminalbattleship.help;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class HelpTest {

    @Test
    void shouldDisplayRulesForHelpLowercase() {
        assertHelpDisplaysRules("help");
    }

    @Test
    void shouldDisplayRulesForHelpUppercase() {
        assertHelpDisplaysRules("HELP");
    }

    @Test
    void shouldDisplayRulesForHelpMixedCase() {
        assertHelpDisplaysRules("HelP");
    }

    @Test
    void shouldDisplayRulesForHelpWithTrailingSpaces() {
        assertHelpDisplaysRules("help   ");
    }

    private void assertHelpDisplaysRules(String input) {
        GameRunner gameRunner = new GameRunner(new MockGameEngine());

        ByteArrayInputStream in = new ByteArrayInputStream((input + "\nquit\n").getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            gameRunner.askForPlayerShot();
        } catch (Exception ignored) {
            // on ignore System.exit() provoqué par "quit"
        }

        String output = outContent.toString();
        assertThat(output).contains("Règles du jeu");
    }

    static class MockGameEngine extends com.qualityworkshop.terminalbattleship.game.GameEngine {
        @Override
        public boolean isComputerFleetDestroyed() { return true; }

        @Override
        public boolean isPlayerFleetDestroyed() { return false; }

        @Override
        public ShotResult playerShoots(String coordinate) {
            return new ShotResult("Tir simulé", false);
        }
    }
}
