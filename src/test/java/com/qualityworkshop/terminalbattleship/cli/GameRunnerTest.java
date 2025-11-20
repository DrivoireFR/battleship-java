package com.qualityworkshop.terminalbattleship.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

import com.qualityworkshop.terminalbattleship.game.ShotResult;

public class GameRunnerTest {


    @Test
    void shouldRejectSpacesEntry() {
        java.io.InputStream originalIn = System.in;

        GameRunner runner = new GameRunner(null);

        String input = "  \n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ShotResult result = runner.askForPlayerShot(true);
        assertThat(result).isNull();


        System.setIn(originalIn);
    }

    @Test
    void shouldRejectEmptyEntry() {
        java.io.InputStream originalIn = System.in;

        GameRunner runner = new GameRunner(null);

        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ShotResult result = runner.askForPlayerShot(true);
        assertThat(result).isNull();


        System.setIn(originalIn);
    }
}
