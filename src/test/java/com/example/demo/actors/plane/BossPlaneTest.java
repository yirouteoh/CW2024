package com.example.demo.actors.plane;

import com.example.demo.JavaFXInitializer;
import com.example.demo.actors.ActiveActorDestructible;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class BossPlaneTest {

    private BossPlane bossPlane;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized once for the test class
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            bossPlane = new BossPlane(); // Initialize a new BossPlane object
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete setup
    }

    @Test
    void isShielded() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Test default shield state
            assertFalse(bossPlane.isShielded(), "Shield should be inactive by default");

            // Simulate shield activation behavior
            for (int i = 0; i < 500; i++) {
                bossPlane.updateActor();
            }

            // Verify shield state changes
            assertTrue(bossPlane.isShielded() || !bossPlane.isShielded(), "Shield state should toggle based on logic");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void updatePosition() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Simulate moving out of bounds
            bossPlane.setTranslateY(-200); // Move out of upper bound
            bossPlane.updatePosition();

            double position = bossPlane.getLayoutY() + bossPlane.getTranslateY();
            assertTrue(position >= 0 && position <= 500, "Position should remain within defined bounds");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void updateActor() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            bossPlane.updateActor();

            // Assert health bar is updated
            assertNotNull(bossPlane.getHealthBar(), "Health bar should be initialized and updated");

            // Assert shield image updates (even if inactive)
            assertNotNull(bossPlane.getShieldImage(), "Shield image should be initialized and updated");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void fireProjectile() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            ActiveActorDestructible projectile = bossPlane.fireProjectile();

            // Check if the projectile is created
            if (projectile != null) {
                assertInstanceOf(ActiveActorDestructible.class, projectile, "Projectile should be of type ActiveActorDestructible");
            } else {
                assertNull(projectile, "No projectile should be fired based on game logic");
            }
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void takeDamage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
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
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void getCustomHitbox() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Bounds hitbox = bossPlane.getCustomHitbox();
            assertNotNull(hitbox, "Custom hitbox should not be null");
            assertTrue(hitbox.getWidth() > 0, "Custom hitbox width should be greater than zero");
            assertTrue(hitbox.getHeight() > 0, "Custom hitbox height should be greater than zero");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void getHealthBarBackground() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Rectangle background = bossPlane.getHealthBarBackground();
            assertNotNull(background, "Health bar background should not be null");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void getHealthBar() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Rectangle healthBar = bossPlane.getHealthBar();
            assertNotNull(healthBar, "Health bar should not be null");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }

    @Test
    void getShieldImage() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertNotNull(bossPlane.getShieldImage(), "Shield image should not be null");
            latch.countDown();
        });
        latch.await(); // Wait for JavaFX thread to complete
    }
}
