package com.qualityworkshop.terminalbattleship.cli;

import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import com.qualityworkshop.terminalbattleship.rendering.BoardRenderer;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class GameRunner {

    private final GameEngine gameEngine;
    private final Scanner scanner;
    private boolean isQuietModeEnabled;

    public GameRunner(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printMessage("Bienvenue dans Terminal Battleship ! Tapez 'quit' pour arrêter.\n", false);
        while (!gameEngine.isComputerFleetDestroyed() && !gameEngine.isPlayerFleetDestroyed()) {
            displayBoards();
            ShotResult playerResult = askForPlayerShot();
            printMessage(playerResult.message(), false);
            if (playerResult.gameOver()) {
                break;
            }

            ShotResult computerResult = gameEngine.computerShoots();
            printMessage("\nOrdinateur: " + computerResult.message(), false);

            if (computerResult.gameOver()) {
                break;
             }
        }
        endGameMessage();
    }

    private void displayBoards() {
        printMessage("\nVotre grille", true);
        printMessage(BoardRenderer.render(gameEngine.playerBoard(), true), true);
        printMessage("\nGrille adverse (brouillard)", true);
        printMessage(BoardRenderer.render(gameEngine.computerBoard(), false), true);
    }

    private ShotResult askForPlayerShot() {
        while (true) {
            printMessage("Entrez une coordonnée (ex: e5): ", true);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) {
                System.exit(0);
            }
            try {
                return gameEngine.playerShoots(input);
            } catch (IllegalArgumentException ex) {
                printMessage("Entrée invalide: " + ex.getMessage(), false);
            }
        }
    }

    private void endGameMessage() {
        if (gameEngine.isComputerFleetDestroyed()) {
            printMessage("\nBravo, vous avez coulé tous les navires adverses!", false);
        } else if (gameEngine.isPlayerFleetDestroyed()) {
            printMessage("\nDommage! L'ordinateur a gagné cette fois.", false);
        } else {
            printMessage("\nPartie interrompue.", false);
        }
    }

    private void printMessage(String message, boolean important){
        if (!isQuietModeEnabled() || important) {
            System.out.println(message);
        }
    }

    public void setQuietMode(boolean quietMode) {
        this.isQuietModeEnabled = quietMode;
    }

    public boolean isQuietModeEnabled(){
        return isQuietModeEnabled;
    }
}
