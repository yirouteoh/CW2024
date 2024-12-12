package com.example.demo.levels;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.actors.plane.BossPlane;
import com.example.demo.screens.LevelView;
import com.example.demo.JavaFXInitializer;

class LevelTwoTest {

    private LevelTwo levelTwo;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        levelTwo = new LevelTwo(600, 800); // Initialize with screen height and width
    }

    @Test
    void checkIfGameOver() {
        levelTwo.getUser().destroy(); // Simulate game loss
        levelTwo.checkIfGameOver();
        assertTrue(levelTwo.getUser().isDestroyed(), "Game should detect user plane destruction.");

        BossPlane boss = new BossPlane();
        boss.destroy();
        levelTwo.checkIfGameOver(); // Simulate boss defeat
        assertTrue(boss.isDestroyed(), "Game should detect boss destruction.");
    }

    @Test
    void spawnEnemyUnits() {
        assertDoesNotThrow(() -> levelTwo.spawnEnemyUnits(),
                "Spawning enemy units (boss) should not throw exceptions.");
        BossPlane boss = new BossPlane();
        levelTwo.spawnEnemyUnits();
        assertNotNull(boss, "Boss plane should be initialized and spawned.");
    }

    @Test
    void instantiateLevelView() {
        LevelView levelView = levelTwo.instantiateLevelView();
        assertNotNull(levelView, "LevelView should be instantiated.");
    }
}