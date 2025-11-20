package com.qualityworkshop.terminalbattleship.rendering;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardRendererSizeTest {

    @Test
    void shouldRenderFourByFourGrid() {
        Board board = Board.withShips(4, List.of(new Coordinate(0, 0)));

        String render = BoardRenderer.render(board, true);

        assertThat(render).contains("A B C D");
        assertThat(render).doesNotContain("E");
        assertThat(render).doesNotContain("F");
        assertThat(render).contains("1 ");
        assertThat(render).contains("2 ");
        assertThat(render).contains("3 ");
        assertThat(render).contains("4 ");
        assertThat(render).doesNotContain("5 ");
        assertThat(render).doesNotContain("6 ");
    }

    @Test
    void shouldRenderSixBySixGrid() {
        Board board = Board.withShips(6, List.of(new Coordinate(0, 0)));

        String render = BoardRenderer.render(board, true);

        assertThat(render).contains("A B C D E F");
        assertThat(render).contains("6 ");
    }

    @Test
    void shouldHaveCorrectLineLengthForMiniGrid() {
        Board board = Board.withShips(4, List.of());

        String render = BoardRenderer.render(board, true);

        String[] lines = render.split("\n");
        for (String line : lines) {
            assertThat(line.length()).isLessThanOrEqualTo(15);
        }
    }
}