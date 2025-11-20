package com.qualityworkshop.terminalbattleship.cli;

import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import com.qualityworkshop.terminalbattleship.rendering.BoardRenderer;
import com.qualityworkshop.terminalbattleship.score.ScoreService;
import org.springframework.stereotype.Component;

import com.qualityworkshop.terminalbattleship.rendering.BoardExporter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        //System.out.println("Bienvenue dans Terminal Battleship ! Tapez 'quit' pour arrêter.\n");

        System.out.println("Bienvenue dans Terminal Battleship !");
        System.out.println("Commandes: 'quit' pour arrêter, 'export' pour sauvegarder\n");


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

    /*private ShotResult askForPlayerShot() {
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
    }*/

    private ShotResult askForPlayerShot() {
        while (true) {
            System.out.print("Entrez une coordonnée (ex: e5): ");
            String input = scanner.nextLine();

            if (input.trim().equalsIgnoreCase("quit")) {
                System.exit(0);
            }

            // ===== US9: Détecter la commande export =====
            if (input.trim().equalsIgnoreCase("export")) {
                handleExport();
                continue; // Redemander une coordonnée
            }

            try {
                return gameEngine.playerShoots(input);
            } catch (IllegalArgumentException ex) {
                if (ex.getMessage().contains("vide")) {
                    System.out.println("⚠️  Vous devez entrer quelque chose! Exemple: A1, B3, E5");
                } else {
                    System.out.println("❌ Entrée invalide: " + ex.getMessage());
                }
            }
        }
    }

    // ===== US9: Nouvelle méthode pour gérer l'export =====
    private void handleExport() {
        try {
            Path targetDir = Path.of("target");

            // Créer le dossier target s'il n'existe pas
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            Path exportedFile = BoardExporter.exportWithTimestamp(
                    gameEngine.playerBoard(),
                    gameEngine.computerBoard(),
                    targetDir
            );

            System.out.println("✅ Plateau exporté avec succès : " + exportedFile.getFileName());
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de l'export : " + e.getMessage());
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
