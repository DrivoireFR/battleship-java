package com.qualityworkshop.terminalbattleship.rendering;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.CellState;

public final class BoardRenderer {

    private BoardRenderer() {
    }

    public static String render(Board board, boolean revealShips) {
        StringBuilder builder = new StringBuilder();
        int size = board.size();

        // En-tête des colonnes
        builder.append("  ");
        for (int col = 0; col < size; col++) {
            builder.append((char)('A' + col));
            if (col < size - 1) {
                builder.append(' ');
            }
        }
        builder.append('\n');

        for (int row = 0; row < size; row++) {
            builder.append(row + 1).append(' ');
            for (int col = 0; col < size; col++) {
                CellState state = board.cellAt(new com.qualityworkshop.terminalbattleship.board.Coordinate(row, col));
                builder.append(state.glyph(revealShips));
                if (col < size - 1) {
                    builder.append(' ');
                }
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}

