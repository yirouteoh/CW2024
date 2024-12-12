package com.example.demo.levels;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.JavaFXInitializer;

class LevelOneTest {

    private LevelOne levelOne;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        levelOne = new LevelOne(600, 800); // Initialize LevelOne with screen height and width
    }

    @Test
    void initializeFriendlyUnits() {
        levelOne.initializeFriendlyUnits();
        assertNotNull(levelOne.getUser(), "User plane should be initialized and added to the scene.");
        assertTrue(levelOne.getRoot().getChildren().contains(levelOne.getUser()),
                "User plane should be present in the scene.");
    }

    @Test
    void instantiateLevelView() {
        assertNotNull(levelOne.instantiateLevelView(), "Level view should be instantiated successfully.");
    }
}
