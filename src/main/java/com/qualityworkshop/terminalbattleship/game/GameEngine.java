package com.qualityworkshop.terminalbattleship.game;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.strategy.ShotStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;


@Component
public class GameEngine {

    private final Board playerBoard;
    private final Board computerBoard;
    private final ShotStrategy computerStrategy;

    // US2 : historiques des coups
    private static final int SHOT_HISTORY_LIMIT = 3;
    private final Deque<ShotResult> lastPlayerShots = new ArrayDeque<>();
    private final Deque<ShotResult> lastComputerShots = new ArrayDeque<>();

    private void recordShot(Deque<ShotResult> history, ShotResult result) {
        history.addFirst(result); // le plus récent en premier
        if (history.size() > SHOT_HISTORY_LIMIT) {
            history.removeLast(); // on garde au max 3
        }
    }

    public GameEngine(@Qualifier("playerBoard") Board playerBoard,
                      @Qualifier("computerBoard") Board computerBoard,
                      ShotStrategy computerStrategy) {
        this.playerBoard = playerBoard;
        this.computerBoard = computerBoard;
        this.computerStrategy = computerStrategy;
    }

    public ShotResult playerShoots(String coordinateInput) {
        Coordinate coordinate = Coordinate.fromInput(coordinateInput);
        ShotOutcome outcome = computerBoard.shoot(coordinate);
        ShotResult result = new ShotResult(
                coordinate,
                outcome,
                !computerBoard.hasRemainingShips(),
                messageFor("Vous", outcome));

        recordShot(lastPlayerShots, result); // US2 : on enregistre le tir du joueur
        return result;
    }

    public ShotResult computerShoots() {
        Coordinate target = computerStrategy.pickTarget(playerBoard);
        ShotOutcome outcome = playerBoard.shoot(target);
        ShotResult result = new ShotResult(
                target,
                outcome,
                !playerBoard.hasRemainingShips(),
                messageFor("L'ordinateur", outcome));

        recordShot(lastComputerShots, result); // US2 : on enregistre le tir de l'IA
        return result;
    }

    // US2 : derniers tirs du joueur (le plus récent en premier)
    public List<ShotResult> lastPlayerShots() {
        return List.copyOf(lastPlayerShots);
    }

    // US2 : derniers tirs de l'ordinateur (le plus récent en premier)
    public List<ShotResult> lastComputerShots() {
        return List.copyOf(lastComputerShots);
    }


    public boolean isComputerFleetDestroyed() {
        return !computerBoard.hasRemainingShips();
    }

    public boolean isPlayerFleetDestroyed() {
        return !playerBoard.hasRemainingShips();
    }

    public Board playerBoard() {
        return playerBoard;
    }

    public Board computerBoard() {
        return computerBoard;
    }

    private String messageFor(String shooter, ShotOutcome outcome) {
        return switch (outcome) {
            case HIT -> shooter + " a touché un navire !";
            case MISS -> shooter + " a tiré dans l'eau.";
            case ALREADY_TARGETED -> shooter + " avait déjà visé cette case.";
            case INVALID -> shooter + " a visé hors de la grille !";
            case ALL_SUNK -> shooter + " vient de couler la dernière cible !";
        };
    }
}
