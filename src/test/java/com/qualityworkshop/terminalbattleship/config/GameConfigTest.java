package com.qualityworkshop.terminalbattleship.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameConfigTest {

    @BeforeEach
    void setUp() {
        // Réinitialiser la valeur avant chaque test pour éviter les interférences
        GameConfig.QUIET_MODE = false;
    }

    @Test
    void quietModeShouldBeSettable() {
        assertFalse(GameConfig.QUIET_MODE, "Le mode silencieux doit être faux par défaut.");
        GameConfig.QUIET_MODE = true;
        assertTrue(GameConfig.QUIET_MODE, "Le mode silencieux aurait dû être mis à vrai.");
    }
}
