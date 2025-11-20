package com.qualityworkshop.terminalbattleship.rendering;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.CellState;

public final class BoardRenderer {

    private BoardRenderer() {
    }

    public static String render(Board board, boolean revealShips) {
        StringBuilder builder = new StringBuilder();
        builder.append("  A B C D E F\n");
        for (int row = 0; row < board.size(); row++) {
            builder.append(row + 1).append(' ');
            for (int col = 0; col < board.size(); col++) {
                CellState state = board.cellAt(new com.qualityworkshop.terminalbattleship.board.Coordinate(row, col));
                builder.append(state.glyph(revealShips));
                if (col < board.size() - 1) {
                    builder.append(' ');
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
