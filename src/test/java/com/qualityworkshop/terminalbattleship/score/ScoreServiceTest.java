package com.qualityworkshop.terminalbattleship.score;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

class ScoreServiceTest {

    @Test
    void whenNoScoreFileExists_shouldReturnZeroScores() {
        ScoreService scoreService = new ScoreService("un-fichier-qui-n-existe-pas.tmp");
        assertEquals(0, scoreService.getWins());
        assertEquals(0, scoreService.getLosses());
    }

    @Test
    void whenIncrementWins_shouldSaveToFile(@TempDir Path tempDir) throws Exception {
        Path scoreFile = tempDir.resolve("scores.txt");

        // 1. Incrémenter la victoire
        ScoreService service1 = new ScoreService(scoreFile.toString());
        service1.incrementWins(); // Cette méthode n'existe pas encore

        // 2. Lire le score avec un nouveau service pour vérifier la sauvegarde
        ScoreService service2 = new ScoreService(scoreFile.toString());
        assertEquals(1, service2.getWins(), "Le nombre de victoires devrait être 1 après sauvegarde");
        assertEquals(0, service2.getLosses());
    }
}
