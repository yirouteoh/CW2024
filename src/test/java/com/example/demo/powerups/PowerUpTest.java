package com.example.demo.powerups;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class PowerUpTest {

    private PowerUp powerUp;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        powerUp = new PowerUp("spreadshot.png", 100, 100); // Initialize with sample image and position
    }

    @Test
    void updateActor() {
        double initialY = powerUp.getTranslateY();
        powerUp.updateActor();
        assertEquals(initialY + 3.0, powerUp.getTranslateY(), "PowerUp should move down by fall speed.");
        assertFalse(powerUp.isDestroyed(), "PowerUp should not be destroyed if within bounds.");

        // Move power-up out of bounds and check destruction
        powerUp.setTranslateY(601);
        powerUp.updateActor();
        assertTrue(powerUp.isDestroyed(), "PowerUp should be destroyed when moving out of bounds.");
    }

    @Test
    void updatePosition() {
        double initialY = powerUp.getTranslateY();
        powerUp.updatePosition();
        assertEquals(initialY + 3.0, powerUp.getTranslateY(), "PowerUp should move down by fall speed.");
    }

    @Test
    void takeDamage() {
        assertFalse(powerUp.isDestroyed(), "PowerUp should not be destroyed initially.");
        powerUp.takeDamage();
        assertTrue(powerUp.isDestroyed(), "PowerUp should be destroyed after taking damage.");
    }
}
