package com.example.demo.actors.projectile;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class EnemyProjectileTest {

    private EnemyProjectile enemyProjectile;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        enemyProjectile = new EnemyProjectile(500, 300); // Initialize with starting position (500, 300)
    }

    @Test
    void updatePosition() {
        double initialX = enemyProjectile.getTranslateX();
        enemyProjectile.updatePosition();
        assertEquals(initialX - 10, enemyProjectile.getTranslateX(), "Projectile should move left by 10 units.");
    }

    @Test
    void updateActor() {
        double initialX = enemyProjectile.getTranslateX();
        enemyProjectile.updateActor();
        assertEquals(initialX - 10, enemyProjectile.getTranslateX(), "updateActor should call updatePosition and move the projectile left.");
    }
}
