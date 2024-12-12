package com.example.demo.actors.plane;

import com.example.demo.JavaFXInitializer;
import com.example.demo.actors.ActiveActorDestructible;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.JavaFXInitializer;
import static org.junit.jupiter.api.Assertions.*;

class EnemyPlaneTest {

    private EnemyPlane enemyPlane;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized once
    }

    @BeforeEach
    void setUp() {
        // Initialize an EnemyPlane with a starting position
        enemyPlane = new EnemyPlane(500, 300);
    }

    @Test
    void fireProjectile() {
        // Call the fireProjectile method multiple times
        ActiveActorDestructible projectile = null;
        for (int i = 0; i < 1000; i++) {
            projectile = enemyPlane.fireProjectile();
            if (projectile != null) break; // If a projectile is fired, stop the loop
        }

        // Assert that a projectile was fired at least once
        assertNotNull(projectile, "EnemyPlane should eventually fire a projectile.");
    }
}
