package com.qualityworkshop.terminalbattleship.game;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.CellState;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.strategy.NoOpComputerStrategy;
import com.qualityworkshop.terminalbattleship.strategy.ShotStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameEngineTest {

    @Test
    void shouldReturnHitAndMarkBoard() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        ShotResult result = engine.playerShoots("A1");

        assertThat(result.outcome()).isEqualTo(ShotOutcome.ALL_SUNK);
        assertThat(computerBoard.cellAt(new Coordinate(0, 0))).isEqualTo(CellState.HIT);
        assertThat(engine.isComputerFleetDestroyed()).isTrue();
    }

    @Test
    void shouldRejectInvalidCoordinate() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        assertThatThrownBy(() -> engine.playerShoots("Z9"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAcceptLowercaseInput() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        ShotResult result = engine.playerShoots("a1");

        assertThat(result.outcome()).isEqualTo(ShotOutcome.ALL_SUNK);
    }

    @Test
    void shouldRejectMixedInput() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        assertThatThrownBy(() -> engine.playerShoots("5E"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldFlagAlreadyTargetedCell() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(1, 1)));

        engine.playerShoots("A1");
        ShotResult secondShot = engine.playerShoots("A1");

        assertThat(secondShot.outcome()).isEqualTo(ShotOutcome.ALREADY_TARGETED);
    }

    @Test
    void shouldAllowComputerToFire() {
        Board playerBoard = Board.withShips(6, List.of(new Coordinate(0, 0)));
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(0, 1)));
        GameEngine engine = new GameEngine(playerBoard, computerBoard, new NoOpComputerStrategy(new Coordinate(0, 0)));

        ShotResult computerShot = engine.computerShoots();

        assertThat(computerShot.outcome()).isEqualTo(ShotOutcome.ALL_SUNK);
        assertThat(playerBoard.cellAt(new Coordinate(0, 0))).isEqualTo(CellState.HIT);
        assertThat(engine.isPlayerFleetDestroyed()).isTrue();
    }

    @BeforeEach
    void resetHistory() {
        GameEngine.HistoriqueTirJoueurAléatoire.clear();
    }

    // Fake strategy pour contrôler les retours successifs
    static class FakeStrategy implements ShotStrategy {
        private final List<Coordinate> sequence;
        private int index = 0;

        FakeStrategy(Coordinate... coords) {
            this.sequence = List.of(coords);
        }

        @Override
        public Coordinate pickTarget(Board board) {
            return sequence.get(index++);
        }
    }

    @Test
    void shouldPickNewRandomCoordinateWhenPreviousAlreadyUsed() {

        // On marque "A1" comme déjà tiré par auto
        GameEngine.HistoriqueTirJoueurAléatoire.add("A1");

        // Fake strategy :
        // 1er retour : A1 → déjà tiré → doit être rejeté
        // 2e retour : B2 → accepté
        ShotStrategy fakeStrategy = new FakeStrategy(
                new Coordinate(0,0),  // A1
                new Coordinate(1,1)   // B2
        );

        Board playerBoard = Board.withShips(6, List.of());
        Board computerBoard = Board.withShips(6, List.of(new Coordinate(5,5)));

        GameEngine engine = new GameEngine(playerBoard, computerBoard, fakeStrategy);

        ShotResult result = engine.playerShootsRandomly();

        // Vérifie que le tir final est bien B2
        assertThat(result.coordinate()).isEqualTo(new Coordinate(1,1));

        // Vérifie que B2 a bien été ajouté à l'historique
        assertThat(GameEngine.HistoriqueTirJoueurAléatoire)
                .containsExactly("A1", "B2");

        // Vérifie que le tir n'est pas un doublon
        assertThat(GameEngine.HistoriqueTirJoueurAléatoire)
                .doesNotHaveDuplicates();
    }
}
