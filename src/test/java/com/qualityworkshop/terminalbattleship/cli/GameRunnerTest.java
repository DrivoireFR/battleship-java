package com.qualityworkshop.terminalbattleship.cli;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotResult;

public class GameRunnerTest {

    @Test
    void shouldReturnDurationAboveSixtySeconds() {
        GameRunner runner = new GameRunner(null);


        long start = 0L;
        long end = 150L;

        double duration = runner.calculerTempsReponseEnSeconde(start, end);
        assertThat(duration).isEqualTo(0.15);

        end = 65000L;
        duration = runner.calculerTempsReponseEnSeconde(start, end);
        assertThat(duration).isEqualTo(65);
    }
}
