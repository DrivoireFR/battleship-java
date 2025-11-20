package com.qualityworkshop.terminalbattleship;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import com.qualityworkshop.terminalbattleship.config.GameConfig;
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TerminalBattleshipApplicationTest {

    // On crée notre propre "faux" GameRunner.
    // Il ne fait rien, sauf nous dire si sa méthode start() a été appelée.
    static class FakeGameRunner extends GameRunner {
        boolean started = false;
        public FakeGameRunner() {
            super(null); // On peut passer null car on ne l'utilisera pas.
        }
        @Override
        public void start() {
            this.started = true;
        }
    }

    private FakeGameRunner fakeGameRunner;
    private TerminalBattleshipApplication application;

    @BeforeEach
    void setUp() {
        GameConfig.QUIET_MODE = false;
        fakeGameRunner = new FakeGameRunner();
        application = new TerminalBattleshipApplication(fakeGameRunner);
    }

    @Test
    void whenAppRunsWithoutArgs_quietModeIsFalse() {
        application.run();
        assertFalse(GameConfig.QUIET_MODE);
        assertTrue(fakeGameRunner.started, "La méthode start() du GameRunner aurait dû être appelée.");
    }

    @Test
    void whenAppRunsWithQuietArg_quietModeIsTrue() {
        application.run("--quiet");
        assertTrue(GameConfig.QUIET_MODE);
        assertTrue(fakeGameRunner.started, "La méthode start() du GameRunner aurait dû être appelée.");
    }
}
