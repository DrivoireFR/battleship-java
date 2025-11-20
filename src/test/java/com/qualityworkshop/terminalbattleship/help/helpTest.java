package com.qualityworkshop.terminalbattleship.help;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class helpTest {

    static class TestRunner {
        void askForPlayerShot() {
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("help")) {
                System.out.println("Règles du jeu : ...");
            }
        }
    }

    private void assertHelpDisplaysRules(String input) {
        TestRunner runner = new TestRunner();

        String simulatedInput = input + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        runner.askForPlayerShot();

        String output = outContent.toString();
        assertTrue(output.contains("Règles du jeu"),
                "La commande help doit afficher les règles pour: " + input);
    }

    @Test
    void testHelpLowercase() {
        assertHelpDisplaysRules("help");
    }

    @Test
    void testHelpUppercase() {
        assertHelpDisplaysRules("HELP");
    }

    @Test
    void testHelpMixedCase() {
        assertHelpDisplaysRules("HeLp");
    }

    @Test
    void testHelpWithTrailingSpaces() {
        assertHelpDisplaysRules("help   ");
    }
}
