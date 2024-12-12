package com.example.demo.managers;

import com.example.demo.JavaFXInitializer;
import com.example.demo.levels.LevelParent;
import com.example.demo.sounds.SoundManager;
import com.example.demo.screens.LevelView;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class PauseManagerTest {

    private PauseManager pauseManager;
    private SoundManager soundManager;
    private Group root;
    private Stage stage;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        soundManager = SoundManager.getInstance(); // Singleton instance
        root = new Group(); // Root group for UI components

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            stage = new Stage();
            stage.setScene(new Scene(root)); // Attach root to a Scene
            stage.show(); // Ensure the stage is visible
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete initialization

        pauseManager = new PauseManager(soundManager, root);
    }

    @Test
    void showPauseScreen() throws InterruptedException {
        LevelParent levelParent = new LevelParentMock(); // Minimal mock implementation

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> pauseManager.showPauseScreen(levelParent),
                    "showPauseScreen should execute without throwing exceptions");

            assertEquals(GameStateManager.GameState.PAUSED, GameStateManager.getInstance().getState(),
                    "Game state should be PAUSED after showing the pause screen");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    private static class LevelParentMock extends LevelParent {

        public LevelParentMock() {
            super("/com/example/demo/images/background1.jpeg", 800, 600, 3, 10);
        }

        @Override
        protected void initializeFriendlyUnits() {
            // No implementation needed for testing
        }

        @Override
        protected void checkIfGameOver() {
            // No implementation needed for testing
        }

        @Override
        protected LevelView instantiateLevelView() {
            return new LevelView(new Group(), 3); // Provide a valid minimal LevelView
        }
    }
}