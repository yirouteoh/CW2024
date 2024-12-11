package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.BossPlane;
import com.example.demo.actors.plane.UserPlane;
import com.example.demo.powerups.PowerUp;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.sounds.SoundManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;

import java.util.List;

/**
 * Manages collision detection and handling between various game objects.
 * <p>
 * This class handles interactions such as:
 * - Enemy penetration of defenses
 * - User projectiles hitting enemies
 * - Enemy projectiles hitting the user
 * - Plane-to-plane collisions
 * - Power-up collection
 * </p>
 */
public class CollisionManager {

    private final ActorManager actorManager;
    private final SoundManager soundManager;
    private final UserPlane user;
    private final Group root;
    private final double screenWidth;

    /**
     * Constructs a {@link CollisionManager} instance.
     *
     * @param actorManager The {@link ActorManager} managing game actors.
     * @param user         The user's plane.
     * @param root         The root {@link Group} for the game scene.
     * @param screenWidth  The width of the screen.
     */
    public CollisionManager(ActorManager actorManager, UserPlane user, Group root, double screenWidth) {
        this.actorManager = actorManager;
        this.soundManager = SoundManager.getInstance();
        this.user = user;
        this.root = root;
        this.screenWidth = screenWidth;
    }

    /**
     * Handles all types of collisions in the game.
     */
    public void handleAllCollisions() {
        handleEnemyPenetration();
        handleUserProjectileCollisions();
        handleEnemyProjectileCollisions();
        handlePlaneCollisions();
        handlePowerUpCollisions();
    }

    /**
     * Handles cases where enemies penetrate the user's defenses.
     * Reduces the user's health and destroys the enemy.
     */
    private void handleEnemyPenetration() {
        for (ActiveActorDestructible enemy : actorManager.getEnemyUnits()) {
            if (enemyHasPenetratedDefenses(enemy)) {
                user.takeDamage();
                enemy.destroy();
            }
        }
    }

    /**
     * Handles collisions between user projectiles and enemy units, including bosses.
     */
    private void handleUserProjectileCollisions() {
        handleCollisions(actorManager.getUserProjectiles(), actorManager.getEnemyUnits());
    }

    /**
     * Handles generic collisions between projectiles and enemies.
     *
     * @param projectiles The list of projectiles to check.
     * @param enemies     The list of enemies to check.
     */
    private void handleCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> enemies) {
        for (ActiveActorDestructible projectile : projectiles) {
            for (ActiveActorDestructible enemy : enemies) {
                if (enemy instanceof BossPlane boss) {
                    if (boss.getCustomHitbox().intersects(projectile.getBoundsInParent())) {
                        if (!boss.isShielded()) {
                            projectile.takeDamage();
                            boss.takeDamage();
                        }
                    }
                } else if (enemy.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
                    projectile.takeDamage();
                    enemy.takeDamage();
                }
            }
        }
    }

    /**
     * Handles collisions between enemy projectiles and the user's plane.
     */
    private void handleEnemyProjectileCollisions() {
        actorManager.getEnemyProjectiles().forEach(projectile -> {
            if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
                user.takeDamage();
                projectile.destroy();
                soundManager.playCrashSound(); // Play crash sound
                shakeScreen(); // Trigger screen shake
            }
        });
    }

    /**
     * Handles plane-to-plane collisions between friendly and enemy units.
     */
    private void handlePlaneCollisions() {
        actorManager.getFriendlyUnits().forEach(friendly ->
                actorManager.getEnemyUnits().forEach(enemy -> handleCollision(friendly, enemy))
        );
    }

    /**
     * Handles a single collision between a friendly and an enemy unit.
     *
     * @param friendly The friendly unit involved in the collision.
     * @param enemy    The enemy unit involved in the collision.
     */
    private void handleCollision(ActiveActorDestructible friendly, ActiveActorDestructible enemy) {
        if (friendly instanceof UserPlane && friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
            friendly.takeDamage();
            enemy.takeDamage();
            soundManager.playCrashSound(); // Play crash sound
            shakeScreen(); // Trigger screen shake
        }
    }

    /**
     * Handles collisions between the user's plane and power-ups.
     * Activates the power-up upon collection and removes it from the scene.
     */
    private void handlePowerUpCollisions() {
        actorManager.getPowerUps().forEach(powerUp -> {
            if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
                if (powerUp instanceof SpreadshotPowerUp spreadshot) {
                    spreadshot.activate(user); // Activate spreadshot
                } else if (powerUp instanceof PowerUp defaultPowerUp) {
                    defaultPowerUp.activate(user); // Activate default power-ups
                }
                powerUp.destroy(); // Remove power-up after collection
            }
        });
    }

    /**
     * Triggers a screen shake effect when a collision occurs.
     */
    private void shakeScreen() {
        final double amplitude = 10; // How far the screen moves
        final int cycles = 5; // Number of back-and-forth movements
        Timeline timeline = new Timeline();
        for (int i = 0; i < cycles; i++) {
            double offset = (i % 2 == 0) ? amplitude : -amplitude; // Alternate directions
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50 * i), e -> root.setTranslateX(offset)));
        }
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50 * cycles), e -> root.setTranslateX(0))); // Reset position
        timeline.play();
    }

    /**
     * Determines if an enemy has penetrated the user's defenses by moving off-screen.
     *
     * @param enemy The enemy to check.
     * @return true if the enemy has moved beyond the screen width, false otherwise.
     */
    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > screenWidth;
    }
}