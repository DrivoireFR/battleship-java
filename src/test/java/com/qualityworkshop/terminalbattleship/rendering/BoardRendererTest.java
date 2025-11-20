package com.qualityworkshop.terminalbattleship.rendering;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardRendererTest {

    @Test
    void shouldHideShipsWhenNotRevealed() {
        Board board = Board.withShips(2, List.of(new Coordinate(0, 0)));

        String render = BoardRenderer.render(board, false);

        assertThat(render).contains("~").doesNotContain("S");
    }

    @Test
    void shouldRevealShipsWhenRequested() {
        Board board = Board.withShips(2, List.of(new Coordinate(0, 0)));

        String render = BoardRenderer.render(board, true);

        assertThat(render).contains("S");
    }

    @Test
    void shouldRespectLineLengthLimit() {
        Board board = Board.withShips(6, List.of());

        String render = BoardRenderer.render(board, true);

        String[] lines = render.split("\n");
        for (String line : lines) {
            assertThat(line.length()).isLessThanOrEqualTo(20);
        }
    }
}
