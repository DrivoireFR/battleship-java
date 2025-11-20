package com.qualityworkshop.terminalbattleship.game;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.CellState;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.strategy.NoOpComputerStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GameEngineTest {

    @Test
    void shouldReturnHitAndMarkBoard() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        ShotResult result = engine.playerShoots("A1");

        assertThat(result.outcome()).isEqualTo(ShotOutcome.ALL_SUNK);
        assertThat(computerBoard.cellAt(new Coordinate(0, 0))).isEqualTo(CellState.HIT);
        assertThat(engine.isComputerFleetDestroyed()).isTrue();
    }

    @Test
    void shouldRejectInvalidCoordinate() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        assertThatThrownBy(() -> engine.playerShoots("Z9"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAcceptLowercaseInput() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        ShotResult result = engine.playerShoots("a1");

        assertThat(result.outcome()).isEqualTo(ShotOutcome.ALL_SUNK);
    }

    @Test
    void shouldRejectMixedInput() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        assertThatThrownBy(() -> engine.playerShoots("5E"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldFlagAlreadyTargetedCell() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        engine.playerShoots("A1");
        ShotResult secondShot = engine.playerShoots("A1");

        assertThat(secondShot.outcome()).isEqualTo(ShotOutcome.ALREADY_TARGETED);
    }

    @Test
    void shouldAllowComputerToFire() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 1)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(0, 0)));

        ShotResult computerShot = engine.computerShoots();

        assertThat(computerShot.outcome()).isEqualTo(ShotOutcome.ALL_SUNK);
        assertThat(playerBoard.cellAt(new Coordinate(0, 0))).isEqualTo(CellState.HIT);
        assertThat(engine.isPlayerFleetDestroyed()).isTrue();
    }
}
