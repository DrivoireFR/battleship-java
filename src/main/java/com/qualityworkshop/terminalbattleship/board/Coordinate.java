package com.qualityworkshop.terminalbattleship.board;

import java.util.Locale;
import java.util.Objects;

public record Coordinate(int row, int column) {

    private static final String COLUMNS = "ABCDEF";
    private static final int MAX_ROW = getMaxRow();

    private static int getMaxRow() {
        String miniBoard = System.getProperty("mini-board");
        return "true".equalsIgnoreCase(miniBoard) ? 4 : 6;
    }

    public Coordinate {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row and column must be positive");
        }
    }

    public static Coordinate fromInput(String input) {
        Objects.requireNonNull(input, "input");
        String trimmed = input.trim().toUpperCase(Locale.ROOT);
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Entrée vide. Format attendu: lettre + nombre (ex: B3)");
        }
        if (trimmed.length() < 2 || trimmed.length() > 3) {
            throw new IllegalArgumentException("Format attendu: lettre + nombre (ex: B3)");
        }
        char columnLetter = trimmed.charAt(0);
        int columnIndex = COLUMNS.indexOf(columnLetter);
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Lettre hors grille: " + columnLetter);
        }
        int rowValue;
        try {
            rowValue = Integer.parseInt(trimmed.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index de ligne invalide");
        }
        if (rowValue < 1 || rowValue > MAX_ROW) {
            throw new IllegalArgumentException("La ligne doit être comprise entre 1 et " + MAX_ROW);
        }
        return new Coordinate(rowValue - 1, columnIndex);
    }

    @Override
    public String toString() {
        char columnLetter = COLUMNS.charAt(column);
        return String.format("%c%d", columnLetter, row + 1);
    }
}