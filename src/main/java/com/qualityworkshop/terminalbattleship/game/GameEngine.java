package com.qualityworkshop.terminalbattleship.game;

import com.qualityworkshop.terminalbattleship.board.Board;
import com.qualityworkshop.terminalbattleship.board.Coordinate;
import com.qualityworkshop.terminalbattleship.strategy.ShotStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GameEngine {

    private final Board playerBoard;
    private final Board computerBoard;
    private final ShotStrategy computerStrategy;
    public static ArrayList<String> HistoIA = new ArrayList<>();
    public static ArrayList<String> HistoJoueur = new ArrayList<>();

    public GameEngine(@Qualifier("playerBoard") Board playerBoard,
                      @Qualifier("computerBoard") Board computerBoard,
                      ShotStrategy computerStrategy) {
        this.playerBoard = playerBoard;
        this.computerBoard = computerBoard;
        this.computerStrategy = computerStrategy;
    }

    public ShotResult playerShoots(String coordinateInput) {
        Coordinate coordinate = Coordinate.fromInput(coordinateInput);
        HistoJoueur.add(coordinate.toString());
        if (HistoJoueur.size() > 3) {
            HistoJoueur.remove(0); // enlève le plus ancien tir
        }
        ShotOutcome outcome = computerBoard.shoot(coordinate);
        return new ShotResult(
                coordinate,
                outcome,
                !computerBoard.hasRemainingShips(),
                messageFor("Vous", outcome));
    }

    public ShotResult computerShoots() {
        Coordinate target = computerStrategy.pickTarget(playerBoard);
        HistoIA.add(target.toString());
        if (HistoIA.size() > 3) {
            HistoIA.remove(0); // enlève le plus ancien tir
        }
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
