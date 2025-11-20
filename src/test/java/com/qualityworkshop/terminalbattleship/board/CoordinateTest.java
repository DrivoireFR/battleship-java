package com.qualityworkshop.terminalbattleship.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoordinateTest {

    // ===== Tests pour US1: Validation Input Vide =====

    @Test
    void shouldRejectEmptyString() {
        assertThatThrownBy(() -> Coordinate.fromInput(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vide");
    }

    @Test
    void shouldRejectOnlySpaces() {
        assertThatThrownBy(() -> Coordinate.fromInput("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vide");
    }

    @Test
    void shouldRejectSingleSpace() {
        assertThatThrownBy(() -> Coordinate.fromInput(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vide");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "    ", "\t", "\n", " \t "})
    void shouldRejectAllWhitespaceVariations(String input) {
        assertThatThrownBy(() -> Coordinate.fromInput(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vide");
    }

    // ===== Tests de non-régression (le code existant doit toujours marcher) =====

    @Test
    void shouldAcceptValidCoordinate() {
        Coordinate coord = Coordinate.fromInput("A1");
        assertThat(coord.row()).isEqualTo(0);
        assertThat(coord.column()).isEqualTo(0);
    }

    @Test
    void shouldAcceptLowercaseInput() {
        Coordinate coord = Coordinate.fromInput("b3");
        assertThat(coord.row()).isEqualTo(2);
        assertThat(coord.column()).isEqualTo(1);
    }

    @Test
    void shouldAcceptInputWithLeadingTrailingSpaces() {
        Coordinate coord = Coordinate.fromInput("  C5  ");
        assertThat(coord.row()).isEqualTo(4);
        assertThat(coord.column()).isEqualTo(2);
    }

    @Test
    void shouldRejectInvalidColumn() {
        assertThatThrownBy(() -> Coordinate.fromInput("Z3"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("hors grille");
    }

    @Test
    void shouldRejectInvalidRow() {
        assertThatThrownBy(() -> Coordinate.fromInput("A7"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ligne");
    }

    @Test
    void shouldRejectInvalidFormat() {
        assertThatThrownBy(() -> Coordinate.fromInput("123"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ===== Tests pour US4: Mode Grille Réduite =====

    @Test
    void shouldAcceptMiniGridCoordinate() {
        Coordinate coord = Coordinate.fromInput("A1", 4);
        assertThat(coord.row()).isEqualTo(0);
        assertThat(coord.column()).isEqualTo(0);
    }

    @Test
    void shouldAcceptD4OnMiniGrid() {
        Coordinate coord = Coordinate.fromInput("D4", 4);
        assertThat(coord.row()).isEqualTo(3);
        assertThat(coord.column()).isEqualTo(3);
    }

    @Test
    void shouldRejectE1OnMiniGrid() {
        assertThatThrownBy(() -> Coordinate.fromInput("E1", 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("hors grille");
    }

    @Test
    void shouldRejectA5OnMiniGrid() {
        assertThatThrownBy(() -> Coordinate.fromInput("A5", 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ligne");
    }

    @Test
    void shouldStillAcceptF6OnNormalGrid() {
        Coordinate coord = Coordinate.fromInput("F6", 6);
        assertThat(coord.row()).isEqualTo(5);
        assertThat(coord.column()).isEqualTo(5);
    }

    @Test
    void shouldRejectG1OnNormalGrid() {
        assertThatThrownBy(() -> Coordinate.fromInput("G1", 6))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("hors grille");
    }
}