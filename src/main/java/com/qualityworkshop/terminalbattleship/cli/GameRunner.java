package com.qualityworkshop.terminalbattleship.cli;

import com.qualityworkshop.terminalbattleship.board.Coordinate; // Nouvelle importation
import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import com.qualityworkshop.terminalbattleship.rendering.BoardRenderer;
import com.qualityworkshop.terminalbattleship.score.ScoreService;
import com.qualityworkshop.terminalbattleship.strategy.RandomShotStrategy;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Component
public class GameRunner {

    private final GameEngine gameEngine;
    private final ScoreService scoreService;
    private final RandomShotStrategy randomShotStrategy;
    private final Scanner scanner;

    public GameRunner(GameEngine gameEngine, ScoreService scoreService, RandomShotStrategy randomShotStrategy) {
        this.gameEngine = gameEngine;
        this.scoreService = scoreService;
        this.randomShotStrategy = randomShotStrategy;
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
            long startTime = System.currentTimeMillis();

            String input = scanner.nextLine().trim();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
            System.out.printf("Réponse en %d secondes.\n", seconds);

            if (input.equalsIgnoreCase("quit")) {
                System.exit(0);
            } else if (input.equalsIgnoreCase("help")) {
                displayHelp();
                continue;
            } else if (input.equalsIgnoreCase("auto")) {
                try {
                    // Correction ici: Utilise pickTarget() qui retourne un Coordinate, puis le convertit en String
                    Coordinate randomCoordinateObj = randomShotStrategy.pickTarget(gameEngine.computerBoard());
                    String randomCoordinate = randomCoordinateObj.toString();
                    System.out.println("Tir automatique: " + randomCoordinate);
                    return gameEngine.playerShoots(randomCoordinate);
                } catch (IllegalStateException ex) {
                    System.out.println("Erreur lors du tir automatique: " + ex.getMessage() + ". Veuillez entrer une coordonnée manuellement.");
                } catch (IllegalArgumentException ex) {
                    System.out.println("Erreur inattendue lors du tir automatique: " + ex.getMessage() + ". Veuillez entrer une coordonnée manuellement.");
                }
            }
            try {
                return gameEngine.playerShoots(input);
            } catch (IllegalArgumentException ex) {
                System.out.println("Entrée invalide: " + ex.getMessage());
            }
        }
    }

    private void displayHelp() {
        System.out.println("\n--- AIDE BATTLESHIP ---");
        System.out.println("Le but du jeu est de couler tous les navires de l'ordinateur.");
        System.out.println("Vous et l'ordinateur tirez à tour de rôle.");
        System.out.println("Pour tirer, entrez une coordonnée au format 'LettreChiffre' (ex: e5, A3).");
        System.out.println("  - 'X' indique un tir manqué.");
        System.out.println("  - 'O' indique un navire touché.");
        System.out.println("  - 'S' indique un navire coulé.");
        System.out.println("Tapez 'quit' pour quitter la partie à tout moment.");
        System.out.println("Tapez 'help' pour afficher cette aide.");
        System.out.println("Tapez 'auto' pour un tir automatique.");
        System.out.println("-----------------------\n");
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
