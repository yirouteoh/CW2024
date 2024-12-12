package com.example.demo.utils;

import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class HealthBarTest {

    private HealthBar healthBar;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        healthBar = new HealthBar(100.0, 20.0); // Initialize HealthBar with width 100 and height 20
    }

    @Test
    void updatePosition() {
        healthBar.updatePosition(50.0, 100.0); // Set position to (50, 100)

        Rectangle bar = healthBar.getHealthBar();
        Rectangle background = healthBar.getHealthBarBackground();

        assertEquals(60.0, bar.getX(), "HealthBar X position should match updated value with offset.");
        assertEquals(80.0, bar.getY(), "HealthBar Y position should match updated value with offset.");
        assertEquals(60.0, background.getX(), "Background X position should match updated value with offset.");
        assertEquals(80.0, background.getY(), "Background Y position should match updated value with offset.");
    }

    @Test
    void updateHealth() {
        healthBar.updateHealth(0.5, 50, 100); // Set health to 50% with current health 50 and max health 100

        Rectangle bar = healthBar.getHealthBar();

        assertEquals(50.0, bar.getWidth(), "HealthBar width should reflect 50% of the maximum width.");
    }

    @Test
    void animateHealth() {
        assertDoesNotThrow(() -> healthBar.animateHealth(0.75),
                "Animating health should not throw any exceptions.");
    }

    @Test
    void getHealthBar() {
        assertNotNull(healthBar.getHealthBar(), "HealthBar should not be null.");
    }

    @Test
    void getHealthBarBackground() {
        assertNotNull(healthBar.getHealthBarBackground(), "HealthBar background should not be null.");
    }
}
