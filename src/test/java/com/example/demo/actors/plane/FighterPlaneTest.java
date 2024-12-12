package com.example.demo.actors.plane;

import com.example.demo.actors.ActiveActorDestructible;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class FighterPlaneTest {

    private FighterPlane testPlane;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        // Inline implementation of abstract class FighterPlane
        testPlane = new FighterPlane("enemyplane.png", 50, 100, 200, 5) {
            @Override
            public ActiveActorDestructible fireProjectile() {
                return new ActiveActorDestructible("enemyplane.png", 10, getProjectileXPosition(20), getProjectileYPosition(0)) {
                    @Override
                    public void updatePosition() {
                        setTranslateX(getTranslateX() + 1);
                    }

                    @Override
                    public void updateActor() {
                        updatePosition();
                    }

                    @Override
                    public void takeDamage() {
                        destroy();
                    }
                };
            }

            @Override
            public void updatePosition() {
                // Example implementation for FighterPlane's updatePosition
                setTranslateY(getTranslateY() + 2); // Moves the plane 2 units vertically
            }

            @Override
            public void updateActor() {
                // Example implementation for FighterPlane's updateActor
                updatePosition();
            }
        };
    }

    @Test
    void takeDamage() {
        assertEquals(5, testPlane.getHealth(), "Initial health should be 5.");
        testPlane.takeDamage();
        assertEquals(4, testPlane.getHealth(), "Health should decrease by 1 after taking damage.");
        for (int i = 0; i < 4; i++) {
            testPlane.takeDamage();
        }
        assertTrue(testPlane.isDestroyed(), "Plane should be destroyed when health reaches zero.");
    }

    @Test
    void getProjectileXPosition() {
        double projectileX = testPlane.getProjectileXPosition(20);
        assertEquals(120, projectileX, "Projectile X position should be 120.");
    }

    @Test
    void getProjectileYPosition() {
        double projectileY = testPlane.getProjectileYPosition(0);
        assertEquals(200, projectileY, "Projectile Y position should be 200.");
    }

    @Test
    void getHealth() {
        assertEquals(5, testPlane.getHealth(), "Initial health should be 5.");
    }

    @Test
    void getMaxHealth() {
        assertEquals(5, testPlane.getMaxHealth(), "Max health should be 5.");
    }
}