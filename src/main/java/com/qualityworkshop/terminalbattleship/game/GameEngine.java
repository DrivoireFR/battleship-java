package com.qualityworkshop.terminalbattleship.game;

import java.util.Optional;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.strategy.ShotStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GameEngine {

    private final Board playerBoard;
    private final Board computerBoard;
    private final ShotStrategy computerStrategy;

    private String IAHistorique = "";

    public String getIAHistorique() {
        return IAHistorique;
    }

    public void UpdateHistorique(String lastShot) {
        if (IAHistorique.length() >= 3 * 2) { // 3 shots
            IAHistorique = IAHistorique.substring(2); // remove oldest shot
        }
        IAHistorique += lastShot;
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
        return new ShotResult(
                coordinate,
                outcome,
                !computerBoard.hasRemainingShips(),
                messageFor("Vous", outcome));
    }

    public ShotResult computerShoots(Optional<Coordinate> forcedTarget) {
        Coordinate target = forcedTarget.orElseGet(() -> computerStrategy.pickTarget(playerBoard));
        UpdateHistorique(target.toString());
        ShotOutcome outcome = playerBoard.shoot(target);
        return new ShotResult(
                target,
                outcome,
                !playerBoard.hasRemainingShips(),
                messageFor("L'ordinateur", outcome));
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
