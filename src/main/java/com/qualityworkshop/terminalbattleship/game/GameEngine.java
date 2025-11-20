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

    // US8
    private int playerHits;
    private int playerMisses;
    private int computerHits;
    private int computerMisses;


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

        // US8 : mise à jour du score joueur
        if (outcome == ShotOutcome.HIT || outcome == ShotOutcome.ALL_SUNK) {
            playerHits++;
        } else if (outcome == ShotOutcome.MISS || outcome == ShotOutcome.INVALID) {
            playerMisses++;
        }

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

        // US8 : mise à jour du score ordi
        if (outcome == ShotOutcome.HIT || outcome == ShotOutcome.ALL_SUNK) {
            computerHits++;
        } else if (outcome == ShotOutcome.MISS || outcome == ShotOutcome.INVALID) {
            computerMisses++;
        }

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

    // US10 : tir auto
    public ShotResult playerAutoShot() {
        var cells = playerBoard.untargetedCells();
        if (cells.isEmpty()) {
            throw new IllegalStateException("Plus aucune case disponible.");
        }

        // Choisir une case random
        Coordinate randomTarget = cells.get(new java.util.Random().nextInt(cells.size()));

        ShotOutcome outcome = computerBoard.shoot(randomTarget);

        ShotResult result = new ShotResult(
                randomTarget,
                outcome,
                !computerBoard.hasRemainingShips(),
                messageFor("Vous (auto)", outcome)
        );

        // Si tu es en US2 : on l’ajoute à l’historique
        recordShot(lastPlayerShots, result);

        return result;
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

    // US8 : score joueur
    public int getPlayerHits() {
        return playerHits;
    }

    public int getPlayerMisses() {
        return playerMisses;
    }

    // US8 : score ordinateur
    public int getComputerHits() {
        return computerHits;
    }

    public int getComputerMisses() {
        return computerMisses;
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
