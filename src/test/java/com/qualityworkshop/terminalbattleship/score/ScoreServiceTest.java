package com.qualityworkshop.terminalbattleship.score;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreServiceTest {

    @Test
    void whenNoScoreFileExists_shouldReturnZeroScores() {
        // Le service de score est initialisé avec un chemin de fichier qui n'existe pas.
        ScoreService scoreService = new ScoreService("un-fichier-qui-n-existe-pas.tmp");

        // On s'attend à ce que le nombre de victoires soit 0.
        assertEquals(0, scoreService.getWins());
        // On s'attend à ce que le nombre de défaites soit 0.
        assertEquals(0, scoreService.getLosses());
    }
}
