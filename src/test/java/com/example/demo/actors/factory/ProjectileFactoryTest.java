package com.example.demo.actors.factory;

import com.example.demo.JavaFXInitializer;
import com.example.demo.actors.projectile.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class ProjectileFactoryTest {

    private double testX;
    private double testY;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        // Set up common test variables
        testX = 0; // Example X position
        testY = 0; // Example Y position
    }

    @Test
    void createProjectile_Boss() {
        Projectile bossProjectile = ProjectileFactory.createProjectile(ProjectileFactory.ProjectileType.BOSS, testX, testY);
        assertNotNull(bossProjectile, "Boss projectile should not be null.");
        assertTrue(bossProjectile instanceof BossProjectile, "Created projectile should be a BossProjectile.");
        assertEquals(testX, bossProjectile.getTranslateX(), "BossProjectile X position should be set correctly.");
        assertEquals(testY, bossProjectile.getTranslateY(), "BossProjectile Y position should be set correctly.");
    }

    @Test
    void createProjectile_Enemy() {
        Projectile enemyProjectile = ProjectileFactory.createProjectile(ProjectileFactory.ProjectileType.ENEMY, testX, testY);
        assertNotNull(enemyProjectile, "Enemy projectile should not be null.");
        assertTrue(enemyProjectile instanceof EnemyProjectile, "Created projectile should be an EnemyProjectile.");
        assertEquals(testX, enemyProjectile.getTranslateX(), "EnemyProjectile X position should be set correctly.");
        assertEquals(testY, enemyProjectile.getTranslateY(), "EnemyProjectile Y position should be set correctly.");
    }

    @Test
    void createProjectile_User() {
        Projectile userProjectile = ProjectileFactory.createProjectile(ProjectileFactory.ProjectileType.USER, testX, testY);
        assertNotNull(userProjectile, "User projectile should not be null.");
        assertTrue(userProjectile instanceof UserProjectile, "Created projectile should be a UserProjectile.");
        assertEquals(testX, userProjectile.getTranslateX(), "UserProjectile X position should be set correctly.");
        assertEquals(testY, userProjectile.getTranslateY(), "UserProjectile Y position should be set correctly.");
    }
}
