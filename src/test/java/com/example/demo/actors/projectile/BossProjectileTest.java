package com.example.demo.actors.projectile;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class BossProjectileTest {

    private BossProjectile bossProjectile;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        bossProjectile = new BossProjectile(300); // Initialize with Y position 300
    }

    @Test
    void updatePosition() {
        double initialX = bossProjectile.getTranslateX();
        bossProjectile.updatePosition();
        assertEquals(initialX - 15, bossProjectile.getTranslateX(), "Projectile should move left by 15 units.");
    }

    @Test
    void updateActor() {
        double initialX = bossProjectile.getTranslateX();
        bossProjectile.updateActor();
        assertEquals(initialX - 15, bossProjectile.getTranslateX(), "updateActor should update the projectile's position.");
    }
}
