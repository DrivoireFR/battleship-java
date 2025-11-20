package com.qualityworkshop.terminalbattleship.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.strategy.NoOpComputerStrategy;

public class GameRunnerTest {


    @Test
    void shouldReturnValidShotHistoric() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));
        GameRunner gameRunner = new GameRunner(engine);

        String coord1 = "A1";
        gameRunner.askForPlayerShot(coord1.describeConstable());
        assertThat(gameRunner.historique).isEqualTo(coord1);

        String coord2 = "A2";
        gameRunner.askForPlayerShot(coord2.describeConstable());
        assertThat(gameRunner.historique).isEqualTo(coord1 + coord2);

        String coord3 = "A3";
        gameRunner.askForPlayerShot(coord3.describeConstable());

        assertThat(gameRunner.historique).isEqualTo(coord1 + coord2 + coord3);

        String coord4 = "A4";
        gameRunner.askForPlayerShot(coord4.describeConstable());

        assertThat(gameRunner.historique).isEqualTo(coord2 + coord3 + coord4);

        gameRunner.askForPlayerShot(coord4.describeConstable());
        gameRunner.askForPlayerShot(coord4.describeConstable());
        assertThat(gameRunner.historique).isEqualTo(coord4 + coord4 + coord4);
    }
}
