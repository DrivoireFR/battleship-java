package com.qualityworkshop.terminalbattleship.board;

public enum CellState {
    FOG('~'),
    SHIP('S'),
    HIT('X'),
    MISS('O');

    private final char glyph;

    CellState(char glyph) {
        this.glyph = glyph;
    }

    public char glyph(boolean revealShips) {
        if (!revealShips && this == SHIP) {
            return FOG.glyph;
        }
        return glyph;
    }
}
