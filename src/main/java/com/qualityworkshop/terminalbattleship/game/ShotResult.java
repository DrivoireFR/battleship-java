package com.qualityworkshop.terminalbattleship.game;

import com.qualityworkshop.terminalbattleship.board.Coordinate;

public record ShotResult(Coordinate coordinate, ShotOutcome outcome, boolean gameOver, String message) {
}
