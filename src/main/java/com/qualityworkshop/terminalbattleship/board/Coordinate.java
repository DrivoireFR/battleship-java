package com.qualityworkshop.terminalbattleship.board;

import java.util.Locale;
import java.util.Objects;

public record Coordinate(int row, int column) {

    private static final String COLUMNS = "ABCDEF";

    public Coordinate {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row and column must be positive");
        }
    }

    public static Coordinate fromInput(String input) {
        Objects.requireNonNull(input, "input");

        // ===== US1: Validation input vide =====
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("L'entrée ne peut pas être vide");
        }

        String upperInput = trimmed.toUpperCase(Locale.ROOT);
        if (upperInput.length() < 2 || upperInput.length() > 3) {
            throw new IllegalArgumentException("Format attendu: lettre + nombre (ex: B3)");
        }
        char columnLetter = upperInput.charAt(0);
        int columnIndex = COLUMNS.indexOf(columnLetter);
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Lettre hors grille: " + columnLetter);
        }
        int rowValue;
        try {
            rowValue = Integer.parseInt(upperInput.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index de ligne invalide");
        }
        if (rowValue < 1 || rowValue > 6) {
            throw new IllegalArgumentException("La ligne doit être comprise entre 1 et 6");
        }
        return new Coordinate(rowValue - 1, columnIndex);
    }

    @Override
    public String toString() {
        char columnLetter = COLUMNS.charAt(column);
        return String.format("%c%d", columnLetter, row + 1);
    }
}
