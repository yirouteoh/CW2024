package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.FighterPlane;
import com.example.demo.actors.plane.BossPlane;

import com.example.demo.actors.factory.FighterPlaneFactory;

import javafx.scene.Group;

import java.util.Random;

/**
 * Manages enemy-related functionalities, such as spawning, firing projectiles, and keeping track of active enemies.
 * This class works with {@link ActorManager} and a factory pattern to create and manage enemies dynamically.
 */
public class EnemyManager {
    private final ActorManager actorManager;
    private final Group root;
    private final FighterPlaneFactory fighterPlaneFactory; // Factory for creating FighterPlane instances
    private final double screenWidth;
    private final double enemyMaximumYPosition;
    private final Random random = new Random();
    private final double spawnProbability; // Probability of spawning an enemy
    private final int maxEnemies; // Maximum number of enemies allowed on the screen

    public EnemyManager(ActorManager actorManager, Group root, FighterPlaneFactory fighterPlaneFactory, double screenWidth, double enemyMaximumYPosition, double spawnProbability, int maxEnemies) {
        this.actorManager = actorManager;
        this.root = root;
        this.fighterPlaneFactory = fighterPlaneFactory;
        this.screenWidth = screenWidth;
        this.enemyMaximumYPosition = enemyMaximumYPosition;
        this.spawnProbability = spawnProbability;
        this.maxEnemies = maxEnemies;
    }

    /**
     * Spawns enemies if conditions are met.
     */
    public void spawnEnemies() {
        if (actorManager.getEnemyUnits().size() < maxEnemies && random.nextDouble() < spawnProbability) {
            ActiveActorDestructible enemy = createEnemy();
            if (enemy != null) {
                actorManager.addEnemyUnit(enemy, root);

                // Check if the spawned enemy is a Boss
                if (enemy instanceof BossPlane boss) {
                    root.getChildren().add(boss);  // Add the boss first
                    root.getChildren().add(boss.getShieldImage());  // Add the shield image after the boss
                }
            }
        }
    }


    /**
     * Handles enemy firing logic.
     */
    public void generateEnemyFire() {
        for (ActiveActorDestructible enemy : actorManager.getEnemyUnits()) {
            if (enemy instanceof FighterPlane fighter) {
                ActiveActorDestructible projectile = fighter.fireProjectile();
                if (projectile != null) {
                    actorManager.addEnemyProjectile(projectile, root);
                }
            }
        }
    }

    /**
     * Creates a new enemy. Customize as needed for different enemy types.
     *
     * @return A new enemy instance.
     */
    private ActiveActorDestructible createEnemy() {
        return fighterPlaneFactory.createEnemy(screenWidth, enemyMaximumYPosition);
    }


    /**
     * Returns the current number of active enemies.
     *
     * @return The number of active enemies.
     */
    public int getCurrentEnemyCount() {
        return actorManager.getEnemyUnits().size();
    }
}
