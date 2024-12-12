package com.example.demo.actors.projectile;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class ProjectileTest {

    private TestProjectile projectile; // Declare the projectile variable as a field

    private static class TestProjectile extends Projectile {
        public TestProjectile(String imageName, int imageHeight, double initialX, double initialY) {
            super(imageName, imageHeight, initialX, initialY);
        }

        @Override
        public void updatePosition() {
            setTranslateX(getTranslateX() + 1); // Example implementation
        }

        @Override
        public void updateActor() {
            updatePosition(); // Example implementation calling updatePosition
        }

        @Override
        public void takeDamage() {
            destroy(); // Example implementation
        }
    }

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        projectile = new TestProjectile("fireball.png", 10, 100, 200); // Initialize with sample parameters
    }

    @Test
    void takeDamage() {
        assertFalse(projectile.isDestroyed(), "Projectile should not be destroyed initially.");
        projectile.takeDamage();
        assertTrue(projectile.isDestroyed(), "Projectile should be destroyed after taking damage.");
    }

    @Test
    void updatePosition() {
        double initialX = projectile.getTranslateX();
        projectile.updatePosition();
        assertEquals(initialX + 1, projectile.getTranslateX(), "Projectile should move by 1 unit.");
    }
}
