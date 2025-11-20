package com.qualityworkshop.terminalbattleship.board;

import com.qualityworkshop.terminalbattleship.board.Board;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CoordinateTest {
    @Test
    void shouldRejectEmptyInput() {
        assertThatThrownBy(() -> Coordinate.fromInput(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Format attendu");
    }

    @Test
    void shouldRejectSpacesOnlyInput() {
        assertThatThrownBy(() -> Coordinate.fromInput("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Format attendu");
    }

}

