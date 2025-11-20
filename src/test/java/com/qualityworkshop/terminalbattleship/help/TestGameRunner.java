package com.qualityworkshop.terminalbattleship.help;

import com.qualityworkshop.terminalbattleship.cli.GameRunner;

class TestGameRunner extends GameRunner {
    public TestGameRunner(helpTest.MockGameEngine engine) {
        super(engine);
    }

    @Override
    protected void exitGame() {
        throw new RuntimeException("exit");
    }
}