package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.Boss;
import com.example.demo.actors.plane.UserPlane;
import com.example.demo.powerups.PowerUp;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.sounds.SoundManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;

import java.util.List;

public class CollisionManager {

    private final ActorManager actorManager;
    private final SoundManager soundManager;
    private final UserPlane user;
    private final Group root;
    private final double screenWidth;


    public CollisionManager(ActorManager actorManager, UserPlane user, Group root, double screenWidth) {
        this.actorManager = actorManager;
        this.soundManager = SoundManager.getInstance();
        this.user = user;
        this.root = root;
        this.screenWidth = screenWidth;
    }

    public void handleAllCollisions() {
        handleEnemyPenetration();
        handleUserProjectileCollisions();
        handleEnemyProjectileCollisions();
        handlePlaneCollisions();
        handlePowerUpCollisions();
    }

    private void handleEnemyPenetration() {
        for (ActiveActorDestructible enemy : actorManager.getEnemyUnits()) {
            if (enemyHasPenetratedDefenses(enemy)) {
                user.takeDamage();
                enemy.destroy();
            }
        }
    }

    private void handleUserProjectileCollisions() {
        handleCollisions(actorManager.getUserProjectiles(), actorManager.getEnemyUnits());
    }

    private void handleCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> enemies) {
        for (ActiveActorDestructible projectile : projectiles) {
            for (ActiveActorDestructible enemy : enemies) {
                if (enemy instanceof Boss boss) {
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


    private void handlePlaneCollisions() {
        actorManager.getFriendlyUnits().forEach(friendly -> {
            actorManager.getEnemyUnits().forEach(enemy -> {
                if (friendly instanceof UserPlane && friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    friendly.takeDamage();
                    enemy.takeDamage();
                    soundManager.playCrashSound(); // Play crash sound
                    shakeScreen(); // Trigger screen shake
                }
            });
        });
    }


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

    private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
        return Math.abs(enemy.getTranslateX()) > screenWidth;
    }
}