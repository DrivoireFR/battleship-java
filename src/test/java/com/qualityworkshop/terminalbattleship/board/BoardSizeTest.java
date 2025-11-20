package com.qualityworkshop.terminalbattleship.board;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardSizeTest {

    @Test
    void shouldCreateFourByFourBoard() {
        Board board = Board.withShips(4, List.of(
                new Coordinate(0, 0),
                new Coordinate(1, 1)
        ));

        assertThat(board.size()).isEqualTo(4);
    }

    @Test
    void shouldCreateSixBySixBoard() {
        Board board = Board.withShips(6, List.of(
                new Coordinate(0, 0),
                new Coordinate(5, 5)
        ));

        assertThat(board.size()).isEqualTo(6);
    }

    @Test
    void shouldHandleUntargetedCellsOnMiniBoard() {
        Board board = Board.withShips(4, List.of(new Coordinate(0, 0)));

        List<Coordinate> untargeted = board.untargetedCells();

        assertThat(untargeted).hasSize(16); // 4x4 = 16 cells
    }

    @Test
    void shouldHandleUntargetedCellsOnNormalBoard() {
        Board board = Board.withShips(6, List.of(new Coordinate(0, 0)));

        List<Coordinate> untargeted = board.untargetedCells();

        assertThat(untargeted).hasSize(36); // 6x6 = 36 cells
    }

    @Test
    void shouldGenerateRandomFleetOnMiniBoard() {
        Board board = Board.randomFleet(4, 2);

        assertThat(board.size()).isEqualTo(4);
        assertThat(board.hasRemainingShips()).isTrue();
    }

}
