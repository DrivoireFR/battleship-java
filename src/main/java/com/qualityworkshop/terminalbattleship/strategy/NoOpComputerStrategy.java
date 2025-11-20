package com.qualityworkshop.terminalbattleship.strategy;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;

public class NoOpComputerStrategy implements ShotStrategy {

    private final Coordinate fixedCoordinate;

    public NoOpComputerStrategy(Coordinate fixedCoordinate) {
        this.fixedCoordinate = fixedCoordinate;
    }

    @Override
    public Coordinate pickTarget(Board opponentBoard) {
        return fixedCoordinate;
    }
}
