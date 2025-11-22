package com.qualityworkshop.terminalbattleship.export;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.GameExport;
import com.qualityworkshop.terminalbattleship.rendering.BoardRenderer;
import com.qualityworkshop.terminalbattleship.strategy.RandomShotStrategy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExportTest {

    @Test
    void saveTest() throws IOException {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0), new Coordinate(2, 4), new Coordinate(4, 5)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(1, 1), new Coordinate(4, 1), new Coordinate(3, 5)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new RandomShotStrategy(new Random(2)));

        engine.playerShoots("e5");
        engine.playerShoots("d3");
        engine.playerShoots("f2");
        engine.playerShoots("a6");
        engine.playerShoots("a2");
        engine.playerShoots("b2");
        engine.playerShoots("b3");
        engine.playerShoots("c4");
        engine.playerShoots("f5");
        engine.playerShoots("c6");
        engine.computerShoots();
        engine.computerShoots();
        engine.computerShoots();
        engine.computerShoots();
        engine.computerShoots();
        engine.computerShoots();
        engine.computerShoots();
        engine.computerShoots();
        engine.computerShoots();
        engine.computerShoots();

        GameExport export = new GameExport(engine);
        String string = export.exportCurrentViewToDefaultFile();

        String expected =
                "Votre grille\n" +
                        BoardRenderer.render(engine.playerBoard(), true) +
                        "\nGrille adverse (brouillard)\n" +
                        BoardRenderer.render(engine.computerBoard(), false);

        assertEquals(expected, string);
    }
}