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
    private final InputTimer inputTimer;
    private static final long TURN_TIMEOUT_SECONDS = 60;

    public GameRunner(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.scanner = new Scanner(System.in);
        this.inputTimer = new InputTimer(TURN_TIMEOUT_SECONDS);
    }

    public void start() {
        System.out.println("Bienvenue dans Terminal Battleship ! Tapez 'quit' pour arrÃªter.\n");
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
        System.out.println("\n--- Historique des 3 derniers coups ---");
        if (gameEngine.shotHistory().isEmpty()) {
            System.out.println("Aucun tir pour l'instant.");
        } else {
            gameEngine.shotHistory().getLastThree().forEach(record ->
                    System.out.println("  " + record));
        }

        System.out.println("\nVotre grille");
        System.out.println(BoardRenderer.render(gameEngine.playerBoard(), true));
        System.out.println("\nGrille adverse (brouillard)");
        System.out.println(BoardRenderer.render(gameEngine.computerBoard(), false));
    }

    private ShotResult askForPlayerShot() {
        while (true) {
            System.out.print("Entrez une coordonnée (ex: e5) ou 'help' [60s]: ");
            try {
                String input = inputTimer.readWithTimeout(() -> scanner.nextLine().trim());
                if (input.equalsIgnoreCase("quit")) {
                    inputTimer.shutdown();
                    System.exit(0);
                }
                if (input.equalsIgnoreCase("help")) {
                    displayHelp();
                    continue;
                }
                return gameEngine.playerShoots(input);
            } catch (java.util.concurrent.TimeoutException e) {
                System.out.println("\n⏱ Temps écoulé! Coup aléatoire joué.");
                java.util.List<com.qualityworkshop.terminalbattleship.board.Coordinate> available =
                        gameEngine.computerBoard().untargetedCells();
                if (!available.isEmpty()) {
                    com.qualityworkshop.terminalbattleship.board.Coordinate autoShot =
                            available.get(new java.util.Random().nextInt(available.size()));
                    return gameEngine.playerShoots(autoShot.toString());
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Entrée invalide: " + ex.getMessage());
            }
        }
    }

    private void displayHelp() {
        System.out.println("\n=== AIDE ===");
        System.out.println("• Format de coordonnée: lettre (A-F) + chiffre (1-6)");
        System.out.println("  Exemples: A1, b3, E5");
        System.out.println("• 'quit' : Quitter la partie");
        System.out.println("• 'help' : Afficher cette aide");
        System.out.println("• Symboles:");
        System.out.println("  ~ = brouillard/eau");
        System.out.println("  S = navire (votre grille uniquement)");
        System.out.println("  X = touché");
        System.out.println("  O = raté");
        System.out.println("============\n");
    }

    private void endGameMessage() {
        if (gameEngine.isComputerFleetDestroyed()) {
            System.out.println("\nBravo, vous avez coulÃ© tous les navires adverses!");
        } else if (gameEngine.isPlayerFleetDestroyed()) {
            System.out.println("\nDommage! L'ordinateur a gagnÃ© cette fois.");
        } else {
            System.out.println("\nPartie interrompue.");
        }
    }
}