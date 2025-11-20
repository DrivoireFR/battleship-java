package com.qualityworkshop.terminalbattleship.rendering;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.CellState;
import com.qualityworkshop.terminalbattleship.board.Coordinate;

public final class BoardRenderer {

    private static final String COLUMNS = "ABCDEF";

    private BoardRenderer() {
    }

    public static String render(Board board, boolean revealShips) {
        StringBuilder builder = new StringBuilder();

        // ===== US4: Header dynamique selon taille du board =====
        builder.append("  ");
        for (int col = 0; col < board.size(); col++) {
            builder.append(COLUMNS.charAt(col));
            if (col < board.size() - 1) {
                builder.append(' ');
            }
        }
        builder.append('\n');

        // ===== Lignes dynamiques =====
        for (int row = 0; row < board.size(); row++) {
            builder.append(row + 1).append(' ');
            for (int col = 0; col < board.size(); col++) {
                CellState state = board.cellAt(new Coordinate(row, col));
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