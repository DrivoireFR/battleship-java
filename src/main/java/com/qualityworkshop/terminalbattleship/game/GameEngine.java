package com.qualityworkshop.terminalbattleship.game;

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
    private final ShotHistory shotHistory;

    public GameEngine(@Qualifier("playerBoard") Board playerBoard,
                      @Qualifier("computerBoard") Board computerBoard,
                      ShotStrategy computerStrategy) {
        this.playerBoard = playerBoard;
        this.computerBoard = computerBoard;
        this.computerStrategy = computerStrategy;
        this.shotHistory = new ShotHistory();
    }

    public ShotResult playerShoots(String coordinateInput) {
        Coordinate coordinate = Coordinate.fromInput(coordinateInput);
        ShotOutcome outcome = computerBoard.shoot(coordinate);
        shotHistory.record("Vous", coordinate, outcome);
        return new ShotResult(
                coordinate,
                outcome,
                !computerBoard.hasRemainingShips(),
                messageFor("Vous", outcome));
    }

    public ShotResult computerShoots() {
        Coordinate target = computerStrategy.pickTarget(playerBoard);
        ShotOutcome outcome = playerBoard.shoot(target);
        shotHistory.record("IA", target, outcome);
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

    public ShotHistory shotHistory() {
        return shotHistory;
    }

    private String messageFor(String shooter, ShotOutcome outcome) {
        return switch (outcome) {
            case HIT -> shooter + " a touchÃ© un navire !";
            case MISS -> shooter + " a tirÃ© dans l'eau.";
            case ALREADY_TARGETED -> shooter + " avait dÃ©jÃ  visÃ© cette case.";
            case INVALID -> shooter + " a visÃ© hors de la grille !";
            case ALL_SUNK -> shooter + " vient de couler la derniÃ¨re cible !";
        };
    }
}