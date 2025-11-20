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

            // Affichage des plateaux
            displayBoards();

            // Tir du joueur
            ShotResult playerResult = askForPlayerShot();
            System.out.println(playerResult.message());

            // Affichage historique joueur
            displayPlayerShotHistory();

            if (playerResult.gameOver()) {
                break;
            }

            // Tir ordinateur
            ShotResult computerResult = gameEngine.computerShoots();
            System.out.println("\nOrdinateur: " + computerResult.message());

            // Affichage historique IA
            displayComputerShotHistory();

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

    private boolean isInputValid(String input) {
        return input != null && !input.trim().isEmpty();
    }

    private ShotResult askForPlayerShot() {
        while (true) {
            System.out.print("Entrez une coordonnée (ex: e5): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.exit(0);
            }

            // US3 : commande help
            if (input.equalsIgnoreCase("help")) {
                displayHelp();
                // on ne tire pas, on redemande une coordonnée
                continue;
            }

            if (!isInputValid(input)) {
                System.out.println("Entrée invalide : vous devez saisir une coordonnée.");
                continue;
            }

            try {
                return gameEngine.playerShoots(input);
            } catch (IllegalArgumentException ex) {
                System.out.println("Entrée invalide: " + ex.getMessage());
            }
        }
    }

    // US3 : affichage de l'aide
    private void displayHelp() {
        System.out.println("\n===== Aide - Règles du jeu =====");
        System.out.println("- Objectif : couler tous les navires de l'adversaire.");
        System.out.println("- Entrez une coordonnée sous la forme lettre + chiffre (ex: A3, e5).");
        System.out.println("- Les lettres représentent les colonnes, les chiffres les lignes.");
        System.out.println("- 'quit' : quitter la partie.");
        System.out.println("- 'help' : afficher cette aide.");
        System.out.println("================================\n");
    }

    // US2 - Affichage historique joueur
    private void displayPlayerShotHistory() {
        System.out.println("\nHistorique des derniers tirs du joueur (max 3) :");
        if (gameEngine.lastPlayerShots().isEmpty()) {
            System.out.println("Aucun tir enregistré.");
            return;
        }
        gameEngine.lastPlayerShots().forEach(shot ->
                System.out.println("- " + shot.coordinate() + " → " + shot.outcome())
        );
        System.out.println("-----------------------------------");
    }

    // US2 - Affichage historique ordinateur
    private void displayComputerShotHistory() {
        System.out.println("\nHistorique des derniers tirs de l'ordinateur (max 3) :");
        if (gameEngine.lastComputerShots().isEmpty()) {
            System.out.println("Aucun tir enregistré.");
            return;
        }
        gameEngine.lastComputerShots().forEach(shot ->
                System.out.println("- " + shot.coordinate() + " → " + shot.outcome())
        );
        System.out.println("-----------------------------------");
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
