package com.qualityworkshop.terminalbattleship.strategy;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;

public interface ShotStrategy {
    Coordinate pickTarget(Board opponentBoard);
}
