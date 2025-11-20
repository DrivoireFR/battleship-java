package com.qualityworkshop.terminalbattleship.cli;

import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import com.qualityworkshop.terminalbattleship.rendering.BoardRenderer;
import com.qualityworkshop.terminalbattleship.score.ScoreService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class GameRunner {

    private final GameEngine gameEngine;
    private final ScoreService scoreService;
    private final Scanner scanner;

    public GameRunner(GameEngine gameEngine, ScoreService scoreService) {
        this.gameEngine = gameEngine;
        this.scoreService = scoreService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Bienvenue dans Terminal Battleship ! Tapez 'quit' pour arrêter.\n");
        System.out.printf("SCORE: %d victoires / %d défaites\n", scoreService.getWins(), scoreService.getLosses());

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

    private ShotResult askForPlayerShot() {
        while (true) {
            System.out.print("Entrez une coordonnée (ex: e5): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) {
                System.exit(0);
            }
            try {
                return gameEngine.playerShoots(input);
            } catch (IllegalArgumentException ex) {
                System.out.println("Entrée invalide: " + ex.getMessage());
            }
        }
    }

    private void endGameMessage() {
        if (gameEngine.isComputerFleetDestroyed()) {
            System.out.println("\nBravo, vous avez coulé tous les navires adverses!");
            scoreService.incrementWins();
        } else if (gameEngine.isPlayerFleetDestroyed()) {
            System.out.println("\nDommage! L'ordinateur a gagné cette fois.");
            scoreService.incrementLosses();
        } else {
            System.out.println("\nPartie interrompue.");
        }
        System.out.printf("Nouveau score: %d victoires / %d défaites\n", scoreService.getWins(), scoreService.getLosses());
    }
}
