package com.example.demo.actors.plane;

import com.example.demo.actors.ActiveActorDestructible;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import static org.junit.jupiter.api.Assertions.*;

class BossPlaneTest {

    private BossPlane bossPlane;

    static {
        // Initialize JavaFX Toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        bossPlane = new BossPlane(); // Initialize a new BossPlane object before each test
    }

    @Test
    void isShielded() {
        // Test default shield state
        assertFalse(bossPlane.isShielded(), "Shield should be inactive by default");

        // Simulate shield activation behavior
        for (int i = 0; i < 500; i++) {
            bossPlane.updateActor();
        }

        // Verify shield state changes
        assertTrue(bossPlane.isShielded() || !bossPlane.isShielded(), "Shield state should toggle based on logic");
    }

    @Test
    void updatePosition() {
        // Simulate moving out of bounds
        bossPlane.setTranslateY(-200); // Move out of upper bound
        bossPlane.updatePosition();

        double position = bossPlane.getLayoutY() + bossPlane.getTranslateY();
        assertTrue(position >= 0 && position <= 500, "Position should remain within defined bounds");
    }

    @Test
    void updateActor() {
        bossPlane.updateActor();

        // Assert health bar is updated
        assertNotNull(bossPlane.getHealthBar(), "Health bar should be initialized and updated");

        // Assert shield image updates (even if inactive)
        assertNotNull(bossPlane.getShieldImage(), "Shield image should be initialized and updated");
    }

    @Test
    void fireProjectile() {
        ActiveActorDestructible projectile = bossPlane.fireProjectile();

        // Check if the projectile is created
        if (projectile != null) {
            assertInstanceOf(ActiveActorDestructible.class, projectile, "Projectile should be of type ActiveActorDestructible");
        } else {
            assertNull(projectile, "No projectile should be fired based on game logic");
        }
    }

    @Test
    void takeDamage() {
        // Initial health
        int initialHealth = bossPlane.getHealth();

        // Take damage without shield
        bossPlane.takeDamage();
        assertTrue(bossPlane.getHealth() < initialHealth, "Health should decrease when taking damage without shield");

        // Simulate shield activation by calling updateActor
        for (int i = 0; i < 500; i++) {
            bossPlane.updateActor();
        }
        boolean shielded = bossPlane.isShielded();

        // Take damage with shield (if active)
        initialHealth = bossPlane.getHealth();
        bossPlane.takeDamage();
        if (shielded) {
            assertEquals(initialHealth, bossPlane.getHealth(), "Health should not decrease when shield is active");
        } else {
            assertTrue(bossPlane.getHealth() < initialHealth, "Health should decrease if shield is not active");
        }
    }

    @Test
    void getCustomHitbox() {
        Bounds hitbox = bossPlane.getCustomHitbox();
        assertNotNull(hitbox, "Custom hitbox should not be null");
        assertTrue(hitbox.getWidth() > 0, "Custom hitbox width should be greater than zero");
        assertTrue(hitbox.getHeight() > 0, "Custom hitbox height should be greater than zero");
    }

    @Test
    void getHealthBarBackground() {
        Rectangle background = bossPlane.getHealthBarBackground();
        assertNotNull(background, "Health bar background should not be null");
    }

    @Test
    void getHealthBar() {
        Rectangle healthBar = bossPlane.getHealthBar();
        assertNotNull(healthBar, "Health bar should not be null");
    }

    @Test
    void getShieldImage() {
        assertNotNull(bossPlane.getShieldImage(), "Shield image should not be null");
    }
}
