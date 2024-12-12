package com.example.demo.actors.projectile;

import com.example.demo.JavaFXInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class UserProjectileTest {

    private UserProjectile userProjectile;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        userProjectile = new UserProjectile(100, 200); // Initialize with starting position (100, 200)
    }

    @Test
    void updatePosition() {
        double initialX = userProjectile.getTranslateX();
        userProjectile.updatePosition();
        assertEquals(initialX + 15, userProjectile.getTranslateX(), "Projectile should move right by 15 units.");
    }

    @Test
    void updateActor() {
        double initialX = userProjectile.getTranslateX();
        userProjectile.updateActor();
        assertEquals(initialX + 15, userProjectile.getTranslateX(), "updateActor should call updatePosition and move the projectile right.");
    }
}
