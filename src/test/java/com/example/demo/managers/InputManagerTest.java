package com.example.demo.managers;

import com.example.demo.actors.plane.UserPlane;
import com.example.demo.levels.LevelParent;
import com.example.demo.screens.LevelView;
import com.example.demo.JavaFXInitializer;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class InputManagerTest {

    private InputManager inputManager;
    private UserPlane user;
    private LevelParent levelParent;
    private Scene scene;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized once
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            user = new UserPlane(3); // Initialize UserPlane with health 3
            levelParent = new LevelParentMock(); // Mock implementation of LevelParent
            Stage stage = new Stage();
            scene = new Scene(new javafx.scene.Group(), 800, 600);
            stage.setScene(scene);
            stage.show();
            inputManager = new InputManager(user, levelParent);
            inputManager.initializeInputHandlers(scene);
            latch.countDown();
        });
        latch.await(); // Ensure JavaFX thread finishes setup
    }

    @Test
    void initializeInputHandlers() {
        assertDoesNotThrow(() -> inputManager.initializeInputHandlers(scene),
                "Input handlers should initialize without exceptions.");
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
            return null; // Return null or a minimal LevelView instance
        }
    }
}