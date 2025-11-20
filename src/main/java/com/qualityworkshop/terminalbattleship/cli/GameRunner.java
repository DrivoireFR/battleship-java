package com.qualityworkshop.terminalbattleship.cli;

import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import com.qualityworkshop.terminalbattleship.rendering.BoardRenderer;
import com.qualityworkshop.terminalbattleship.history.HistoryTracker;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class GameRunner {

    private final GameEngine gameEngine;
    private final Scanner scanner;

    // Historique des coups
    private final HistoryTracker playerHistory = new HistoryTracker(3);
    private final HistoryTracker computerHistory = new HistoryTracker(3);

    public GameRunner(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Bienvenue dans Terminal Battleship ! Tapez 'quit' pour arrêter.\n");
        while (!gameEngine.isComputerFleetDestroyed() && !gameEngine.isPlayerFleetDestroyed()) {
            displayBoards();
            displayHistory(); // afficher l'historique avant chaque tir

            ShotResult playerResult = askForPlayerShot();
            System.out.println(playerResult.message());
            playerHistory.add(playerResult.coordinate() + " -> " + playerResult.outcome());

            if (playerResult.gameOver()) {
                break;
            }

            ShotResult computerResult = gameEngine.computerShoots();
            System.out.println("\nOrdinateur: " + computerResult.message());
            computerHistory.add(computerResult.coordinate() + " -> " + computerResult.outcome());

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

    private void displayHistory() {
        System.out.println("\nHistorique des 3 derniers coups :");
        System.out.println("Joueur :");
        playerHistory.getHistory().forEach(entry -> System.out.println("  " + entry));
        System.out.println("Ordinateur :");
        computerHistory.getHistory().forEach(entry -> System.out.println("  " + entry));
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
        } else if (gameEngine.isPlayerFleetDestroyed()) {
            System.out.println("\nDommage! L'ordinateur a gagné cette fois.");
        } else {
            System.out.println("\nPartie interrompue.");
        }
    }
}
