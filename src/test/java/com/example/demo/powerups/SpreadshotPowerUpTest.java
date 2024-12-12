package com.example.demo.powerups;

import com.example.demo.actors.plane.UserPlane;
import com.example.demo.levels.LevelParent;
import com.example.demo.screens.LevelView;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class SpreadshotPowerUpTest {

    private SpreadshotPowerUp powerUp;
    private UserPlane userPlane;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        powerUp = new SpreadshotPowerUp(100, 200); // Initialize power-up
        userPlane = new UserPlane(5); // Initialize UserPlane with health
        userPlane.setLevelParent(new LevelParent("/com/example/demo/images/background1.jpeg", 800, 600, 3, 10) {
            @Override
            protected void initializeFriendlyUnits() {
                // No implementation needed for the test
            }

            @Override
            protected void checkIfGameOver() {
                // No implementation needed for the test
            }

            @Override
            protected LevelView instantiateLevelView() {
                return new LevelView(new Group(), 3); // Provide a minimal LevelView instance
            }
        }); // Ensure LevelParent is set for UserPlane
    }

    @Test
    void activate() {
        assertFalse(userPlane.isDestroyed(), "UserPlane should not be destroyed initially.");

        powerUp.activate(userPlane);

        assertTrue(powerUp.isDestroyed(), "PowerUp should be destroyed after activation.");
        // Optionally verify userPlane behavior (e.g., spreadshot effect)
        assertNotNull(userPlane.fireProjectile(), "UserPlane should successfully fire a projectile.");
    }
}
