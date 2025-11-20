package com.qualityworkshop.terminalbattleship.cli;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


public class GameRunnerTest {

    @Test
    void testIsHelp() {
        GameRunner runner = new GameRunner(null);

        assertThat(runner.isHelp("help")).isTrue();
        assertThat(runner.isHelp("HELP")).isTrue();
        assertThat(runner.isHelp("   help   ")).isTrue();
        assertThat(runner.isHelp("hel p")).isFalse();
        assertThat(runner.isHelp("pleh")).isFalse();
    }
}
