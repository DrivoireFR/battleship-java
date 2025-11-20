package com.qualityworkshop.terminalbattleship.cli;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameRunnerTest {


    @Test
    void shouldReturnDurationAboveSixtySeconds() {
        GameRunner runner = new GameRunner(null);


        long start = 0L;
        long end = 65000L; // 65 secondes plus tard

        long duration = runner.calculerTempsReponse(start, end);

        assertThat(duration).isEqualTo(65);
    }


}
