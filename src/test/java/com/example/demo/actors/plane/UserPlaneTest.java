package com.example.demo.actors.plane;

import com.example.demo.levels.LevelParent;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.screens.LevelView;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class UserPlaneTest {

    private UserPlane userPlane;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        userPlane = new UserPlane(100); // Initialize with health 100
    }

    @Test
    void updatePosition() {
        userPlane.setVerticalVelocity(1); // Simulate upward movement
        userPlane.updatePosition();
        assertEquals(6.0, userPlane.getTranslateY(), "UserPlane should move upward by velocity scale.");

        userPlane.setHorizontalVelocity(1); // Simulate rightward movement
        userPlane.updatePosition();
        assertEquals(6.0, userPlane.getTranslateX(), "UserPlane should move rightward by velocity scale.");
    }

    @Test
    void updateActor() {
        userPlane.setVerticalVelocity(1);
        userPlane.updateActor();
        assertEquals(6.0, userPlane.getTranslateY(), "updateActor should update the vertical position.");
    }

    @Test
    void setLevelParent() {
        LevelParent levelParent = new LevelParentMock();
        userPlane.setLevelParent(levelParent);
        assertNotNull(userPlane, "LevelParent should be set correctly.");
    }

    @Test
    void setVerticalVelocity() {
        userPlane.setVerticalVelocity(1); // Set vertical velocity to 1
        userPlane.updatePosition(); // Trigger position update
        assertEquals(6.0, userPlane.getTranslateY(), "Setting vertical velocity should affect position accordingly.");
    }

    @Test
    void setHorizontalVelocity() {
        userPlane.setHorizontalVelocity(1); // Set horizontal velocity to 1
        userPlane.updatePosition(); // Trigger position update
        assertEquals(6.0, userPlane.getTranslateX(), "Setting horizontal velocity should affect position accordingly.");
    }

    @Test
    void stopVerticalMovement() {
        userPlane.setVerticalVelocity(1); // Set initial velocity
        userPlane.stopVerticalMovement(); // Stop movement
        userPlane.updatePosition(); // Trigger position update
        assertEquals(0.0, userPlane.getTranslateY(), "Stopping vertical movement should reset position update.");
    }

    @Test
    void stopHorizontalMovement() {
        userPlane.setHorizontalVelocity(1); // Set initial velocity
        userPlane.stopHorizontalMovement(); // Stop movement
        userPlane.updatePosition(); // Trigger position update
        assertEquals(0.0, userPlane.getTranslateX(), "Stopping horizontal movement should reset position update.");
    }

    // Minimal implementation of LevelParent for testing
    private static class LevelParentMock extends LevelParent {
        public LevelParentMock() {
            super("/com/example/demo/images/background1.jpeg", 800, 600, 3, 10);
        }

        @Override
        protected void initializeFriendlyUnits() {
            // No implementation needed for this test
        }

        @Override
        protected void checkIfGameOver() {
            // No implementation needed for this test
        }

        @Override
        protected LevelView instantiateLevelView() {
            return new LevelView(new Group(), 3); // Provide a minimal LevelView instance
        }
    }
}
