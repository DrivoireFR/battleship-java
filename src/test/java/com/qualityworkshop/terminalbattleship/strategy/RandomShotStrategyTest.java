package com.qualityworkshop.terminalbattleship.strategy;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RandomShotStrategyTest {

    @Test
    void shouldThrowWhenNoRemainingCells() {
        Board board = Board.withShips(1, List.of());
        board.shoot(new Coordinate(0, 0));
        RandomShotStrategy strategy = new RandomShotStrategy(new Random(0));

        assertThatThrownBy(() -> strategy.pickTarget(board))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No remaining targets");
    }

    @Test
    void shouldAvoidAlreadyTargetedCells() {
        Board board = Board.withShips(2, List.of());
        board.shoot(new Coordinate(0, 0));
        Random deterministicRandom = new Random(0);
        RandomShotStrategy strategy = new RandomShotStrategy(deterministicRandom);

        Coordinate target = strategy.pickTarget(board);

        assertThat(target).isNotEqualTo(new Coordinate(0, 0));
    }
}
