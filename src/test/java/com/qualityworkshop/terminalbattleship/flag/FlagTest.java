package com.qualityworkshop.terminalbattleship.flag;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(args = "--quiet", properties = "app.disableRunner=true")
class QuietFlagTest {

    @Autowired
    private GameRunner gameRunner;

    @Test
    void quietFlagShouldBeEnabled() {
        assertTrue(gameRunner.isQuietModeEnabled());
    }
}

@SpringBootTest(args = "", properties = "app.disableRunner=true")
class NoFlagTest {

    @Autowired
    private GameRunner gameRunner;

    @Test
    void quietFlagShouldBeDisabled() {
        assertFalse(gameRunner.isQuietModeEnabled());
    }
}

@SpringBootTest(args = {"--quiet", "--mini-board"}, properties = "app.disableRunner=true")
class UselessFlagTest {

    @Autowired
    private GameRunner gameRunner;

    @Test
    void quietFlagShouldBeEnabledEvenWithOtherFlags() {
        assertTrue(gameRunner.isQuietModeEnabled());
    }
}
