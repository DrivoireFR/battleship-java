package com.qualityworkshop.terminalbattleship.auto;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class autoTest {

    @Test
    void autoShotShouldReturnAValidResult() {
        Board computer = Board.withShips(5, java.util.List.of(
                new Coordinate(0,0),
                new Coordinate(1,1),
                new Coordinate(2,2)
        ));
        Board player = Board.withShips(5, java.util.List.of(
                new Coordinate(0,0),
                new Coordinate(1,1),
                new Coordinate(2,2)
        ));

        GameEngine engine = new GameEngine(player, computer, null);

        ShotResult result = engine.playerShootsRandom();

        assertNotNull(result);
        assertNotNull(result.coordinate());
        assertNotNull(result.message());
    }

    @Test
    void autoShouldNeverShootTheSameCellTwice() {
        Board computer = Board.withShips(10, List.of(
                new Coordinate(0,0), new Coordinate(1,1), new Coordinate(2,2),
                new Coordinate(3,3), new Coordinate(4,4)
        ));
        Board player = Board.withShips(5, java.util.List.of());

        GameEngine engine = new GameEngine(player, computer, null);

        Set<Coordinate> seen = new HashSet<>();

        while (!engine.isComputerFleetDestroyed() && !engine.isPlayerFleetDestroyed()) {
            ShotResult result = engine.playerShootsRandom();
            assertFalse(seen.contains(result.coordinate()), "La case a été ciblée deux fois !");
            seen.add(result.coordinate());
        }

        assertFalse(engine.isComputerFleetDestroyed());

        assertThrows(IllegalStateException.class, engine::playerShootsRandom);
    }


    @Test
    void autoShouldFailWhenGameIsAlreadyOver() {
        Board computer = Board.withShips(5, java.util.List.of());
        Board player = Board.withShips(5, java.util.List.of());

        GameEngine engine = new GameEngine(player, computer, null);

        assertThrows(IllegalStateException.class, engine::playerShootsRandom);
    }
}
