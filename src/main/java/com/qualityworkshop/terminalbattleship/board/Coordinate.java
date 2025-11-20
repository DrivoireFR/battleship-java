package com.qualityworkshop.terminalbattleship.board;

import java.util.Locale;
import java.util.Objects;

public record Coordinate(int row, int column) {

    private static final String COLUMNS = "ABCDEF";
    private static final int DEFAULT_BOARD_SIZE = 6;

    public Coordinate {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row and column must be positive");
        }
    }

    // ===== Méthode par défaut (6x6) - Appelle la version dynamique =====
    public static Coordinate fromInput(String input) {
        return fromInput(input, DEFAULT_BOARD_SIZE);
    }

    // ===== US4: Méthode avec taille paramétrable =====
    public static Coordinate fromInput(String input, int boardSize) {
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

        // ===== US4: Validation basée sur boardSize =====
        if (columnIndex < 0 || columnIndex >= boardSize) {
            throw new IllegalArgumentException("Lettre hors grille: " + columnLetter);
        }

        int rowValue;
        try {
            rowValue = Integer.parseInt(upperInput.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index de ligne invalide");
        }

        // ===== US4: Validation basée sur boardSize =====
        if (rowValue < 1 || rowValue > boardSize) {
            throw new IllegalArgumentException(
                    "La ligne doit être comprise entre 1 et " + boardSize
            );
        }

        return new Coordinate(rowValue - 1, columnIndex);
    }

    @Override
    public String toString() {
        char columnLetter = COLUMNS.charAt(column);
        return String.format("%c%d", columnLetter, row + 1);
    }
}