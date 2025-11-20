package com.qualityworkshop.terminalbattleship.score;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class ScoreService {

    private final Path scoreFilePath;
    private final Properties properties = new Properties();

    public ScoreService() {
        this("battleship_scores.properties"); // Fichier par défaut
    }

    public ScoreService(String filePath) {
        this.scoreFilePath = Paths.get(filePath);
        loadScores();
    }

    private void loadScores() {
        if (Files.exists(scoreFilePath)) {
            try {
                properties.load(Files.newInputStream(scoreFilePath));
            } catch (IOException e) {
                properties.clear();
            }
        }
    }

    private void saveScores() {
        try {
            properties.store(Files.newOutputStream(scoreFilePath), "Battleship Scores");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des scores: " + e.getMessage());
        }
    }

    public int getWins() {
        return Integer.parseInt(properties.getProperty("wins", "0"));
    }

    public int getLosses() {
        return Integer.parseInt(properties.getProperty("losses", "0"));
    }

    public void incrementWins() {
        int wins = getWins() + 1;
        properties.setProperty("wins", String.valueOf(wins));
        saveScores();
    }

    public void incrementLosses() {
        int losses = getLosses() + 1;
        properties.setProperty("losses", String.valueOf(losses));
        saveScores();
    }
}
