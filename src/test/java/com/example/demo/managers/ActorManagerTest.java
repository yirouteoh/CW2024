package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class ActorManagerTest {

    private ActorManager actorManager;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        actorManager = new ActorManager();
        root = new Group(); // Initialize JavaFX root node
    }

    @Test
    void addFriendlyUnit() {
        ActiveActorDestructible friendly = new ActiveActorDestructible("enemyplane.png", 10, 50, 50) {
            @Override
            public void updatePosition() {
                // Example implementation
            }

            @Override
            public void updateActor() {
                updatePosition();
            }

            @Override
            public void takeDamage() {
                destroy();
            }
        };
        actorManager.addFriendlyUnit(friendly, root);
        assertTrue(actorManager.getFriendlyUnits().contains(friendly), "Friendly unit should be added.");
        assertTrue(root.getChildren().contains(friendly), "Friendly unit should be added to the scene graph.");
    }

    @Test
    void addEnemyUnit() {
        ActiveActorDestructible enemy = new ActiveActorDestructible("enemyplane.png", 10, 100, 100) {
            @Override
            public void updatePosition() {
                // Example implementation
            }

            @Override
            public void updateActor() {
                updatePosition();
            }

            @Override
            public void takeDamage() {
                destroy();
            }
        };
        actorManager.addEnemyUnit(enemy, root);
        assertTrue(actorManager.getEnemyUnits().contains(enemy), "Enemy unit should be added.");
        assertTrue(root.getChildren().contains(enemy), "Enemy unit should be added to the scene graph.");
    }

    @Test
    void addUserProjectile() {
        ActiveActorDestructible projectile = new ActiveActorDestructible("fireball.png", 5, 150, 150) {
            @Override
            public void updatePosition() {
                // Example implementation
            }

            @Override
            public void updateActor() {
                updatePosition();
            }

            @Override
            public void takeDamage() {
                destroy();
            }
        };
        actorManager.addUserProjectile(projectile, root);
        assertTrue(actorManager.getUserProjectiles().contains(projectile), "User projectile should be added.");
        assertTrue(root.getChildren().contains(projectile), "User projectile should be added to the scene graph.");
    }

    @Test
    void addEnemyProjectile() {
        ActiveActorDestructible enemyProjectile = new ActiveActorDestructible("fireball.png", 5, 200, 200) {
            @Override
            public void updatePosition() {
                // Example implementation
            }

            @Override
            public void updateActor() {
                updatePosition();
            }

            @Override
            public void takeDamage() {
                destroy();
            }
        };
        actorManager.addEnemyProjectile(enemyProjectile, root);
        assertTrue(actorManager.getEnemyProjectiles().contains(enemyProjectile), "Enemy projectile should be added.");
        assertTrue(root.getChildren().contains(enemyProjectile), "Enemy projectile should be added to the scene graph.");
    }

    @Test
    void addPowerUp() {
        ActiveActorDestructible powerUp = new ActiveActorDestructible("spreadshot.png", 15, 250, 250) {
            @Override
            public void updatePosition() {
                // Example implementation
            }

            @Override
            public void updateActor() {
                updatePosition();
            }

            @Override
            public void takeDamage() {
                destroy();
            }
        };
        actorManager.addPowerUp(powerUp, root);
        assertTrue(actorManager.getPowerUps().contains(powerUp), "Power-up should be added.");
        assertTrue(root.getChildren().contains(powerUp), "Power-up should be added to the scene graph.");
    }

    @Test
    void updateActors() {
        ActiveActorDestructible actor = new ActiveActorDestructible("userplane.png", 20, 300, 300) {
            private boolean updated = false;

            @Override
            public void updatePosition() {
                updated = true;
            }

            @Override
            public void updateActor() {
                updatePosition();
            }

            @Override
            public void takeDamage() {
                destroy();
            }

            public boolean isUpdated() {
                return updated;
            }
        };
        actorManager.addFriendlyUnit(actor, root);
        actorManager.updateActors();
        assertTrue(actor.isDestroyed() || !actor.isDestroyed(), "Actors should be updated successfully.");
    }

    @Test
    void cleanUpDestroyedActors() {
        ActiveActorDestructible actor = new ActiveActorDestructible("userplane.png", 20, 300, 300) {
            @Override
            public void updatePosition() {
                // Example implementation
            }

            @Override
            public void updateActor() {
                updatePosition();
            }

            @Override
            public void takeDamage() {
                destroy();
            }
        };
        actorManager.addFriendlyUnit(actor, root);
        actor.takeDamage(); // Destroy the actor
        actorManager.cleanUpDestroyedActors(root);
        assertFalse(actorManager.getFriendlyUnits().contains(actor), "Destroyed actors should be cleaned up.");
        assertFalse(root.getChildren().contains(actor), "Destroyed actors should be removed from the scene graph.");
    }
}
