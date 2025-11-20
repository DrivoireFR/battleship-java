package com.qualityworkshop.terminalbattleship.cli;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;



public class GameRunnerTest {

    @Test
    void shouldPrintRulesWhenHelpCommand() {
        GameRunner runner = new GameRunner(null);


        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            runner.verifierInputJoueur("help");
        } finally {
            System.setOut(originalOut);
        }

        String output = outContent.toString();
        assertThat(output).contains("=== RÈGLES DU JEU ===");
        assertThat(output).contains("Le but est de couler les navires de l'adversaire.");
        assertThat(output).contains("Les commandes possibles sont :");
        assertThat(output).contains("help : afficher ces règles.");
        assertThat(output).contains("quit : quitter le jeu.");
    }
}
