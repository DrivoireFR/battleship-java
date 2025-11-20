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

    public GameRunner(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Bienvenue dans Terminal Battleship ! Tapez 'quit' pour arrêter.\n");
        while (!gameEngine.isComputerFleetDestroyed() && !gameEngine.isPlayerFleetDestroyed()) {
            displayBoards();
            ShotResult playerResult = askForPlayerShot();
            System.out.println(playerResult.message());
            if (playerResult.gameOver()) {
                break;
            }

            ShotResult computerResult = gameEngine.computerShoots();
            System.out.println("\nOrdinateur: " + computerResult.message());
            if (computerResult.gameOver()) {
                break;
            }
        }
        endGameMessage();
    }

    private void displayBoards() {
        System.out.println("\nVotre grille");
        System.out.println(BoardRenderer.render(gameEngine.playerBoard(), true));
        System.out.println("\nGrille adverse (brouillard)");
        System.out.println(BoardRenderer.render(gameEngine.computerBoard(), false));
    }

    ShotResult askForPlayerShot() {
        long start = System.currentTimeMillis();
        while (true) {
            System.out.print("Entrez une coordonnée (ex: e5): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) {
                System.exit(0);
            }
            try {
                long end = System.currentTimeMillis();
                System.out.println("Réponse en " + calculerTempsReponseEnSeconde(start, end) + " secondes");
                return gameEngine.playerShoots(input);
            } catch (IllegalArgumentException ex) {
                System.out.println("Entrée invalide: " + ex.getMessage());
            }
        }
    }

    private void endGameMessage() {
        if (gameEngine.isComputerFleetDestroyed()) {
            System.out.println("\nBravo, vous avez coulé tous les navires adverses!");
        } else if (gameEngine.isPlayerFleetDestroyed()) {
            System.out.println("\nDommage! L'ordinateur a gagné cette fois.");
        } else {
            System.out.println("\nPartie interrompue.");
        }
    }

    public double calculerTempsReponseEnSeconde(long start, long end){
        return (double) (end - start) / 1000;
    }
}
