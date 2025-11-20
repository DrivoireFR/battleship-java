package com.qualityworkshop.terminalbattleship.cli;

import com.qualityworkshop.terminalbattleship.game.GameEngine;
import com.qualityworkshop.terminalbattleship.game.ShotResult;
import com.qualityworkshop.terminalbattleship.rendering.BoardRenderer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class GameRunner {

    private final GameEngine gameEngine;
    private final Scanner scanner;
    public String historique = "";


    public void UpdateHistorique(String lastShot) {
        if (historique.length() >= 3 * 2) { // 3 shots
            historique = historique.substring(2); // remove oldest shot
        }
        historique += lastShot;
    }

    public GameRunner(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Bienvenue dans Terminal Battleship ! Tapez 'quit' pour arrêter.\n");
        while (!gameEngine.isComputerFleetDestroyed() && !gameEngine.isPlayerFleetDestroyed()) {
            displayBoards();
            ShotResult playerResult = askForPlayerShot(Optional.empty());
            System.out.println(playerResult.message());
            if (playerResult.gameOver()) {
                break;
            }

            ShotResult computerResult = gameEngine.computerShoots(Optional.empty());
            System.out.println("\nOrdinateur: " + computerResult.message());
            if (computerResult.gameOver()) {
                break;
            }
        }
        endGameMessage();
    }

    public void displayBoards() {
        System.out.println("\nVotre grille");
        System.out.println(BoardRenderer.render(gameEngine.playerBoard(), true));
        System.out.println("\nGrille adverse (brouillard)");
        System.out.println(BoardRenderer.render(gameEngine.computerBoard(), false));
        if (!historique.isEmpty()) {
            System.out.println("\n Historique du joueur : " + historique);
            System.out.println("\n Historique de l'IA : " + gameEngine.getIAHistorique() + "\n");
        }
    }

    public ShotResult askForPlayerShot(Optional<String> in) {
        String input = in.orElse("");

        while (true) {
            System.out.print("Entrez une coordonnée (ex: e5): ");
            if (in.isEmpty()) {
                input = scanner.nextLine().trim();
            }
            if (input.equalsIgnoreCase("quit")) {
                System.exit(0);
            }
            try {
                ShotResult shotResult = gameEngine.playerShoots(input);
                if (shotResult != null)
                {
                    UpdateHistorique(input);
                }
                return shotResult;
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
