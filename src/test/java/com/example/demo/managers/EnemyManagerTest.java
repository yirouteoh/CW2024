package com.example.demo.managers;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.FighterPlane;
import com.example.demo.actors.factory.FighterPlaneFactory;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.demo.JavaFXInitializer;

class EnemyManagerTest {

    private EnemyManager enemyManager;
    private ActorManager actorManager;
    private Group root;

    @BeforeAll
    static void initializeJavaFX() {
        JavaFXInitializer.initialize(); // Ensure JavaFX is initialized
    }

    @BeforeEach
    void setUp() {
        actorManager = new ActorManager(); // Initialize ActorManager
        root = new Group(); // Initialize JavaFX root node
        FighterPlaneFactory fighterPlaneFactory = new FighterPlaneFactory() {
            @Override
            public FighterPlane createEnemy(double x, double y) {
                return new FighterPlane("enemyplane.png", 50, x, y, 3) {
                    @Override
                    public ActiveActorDestructible fireProjectile() {
                        return new ActiveActorDestructible("fireball.png", 10, getTranslateX(), getTranslateY()) {
                            @Override
                            public void updatePosition() {
                                setTranslateY(getTranslateY() + 2); // Example implementation
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
                    }

                    @Override
                    public void updatePosition() {
                        setTranslateX(getTranslateX() + 1); // Example movement
                    }

                    @Override
                    public void updateActor() {
                        updatePosition(); // Update actor by updating position
                    }
                };
            }
        };
        enemyManager = new EnemyManager(actorManager, root, fighterPlaneFactory, 800, 600, 0.5, 5);
    }

    @Test
    void spawnEnemies() {
        int initialCount = enemyManager.getCurrentEnemyCount();
        enemyManager.spawnEnemies();
        assertTrue(enemyManager.getCurrentEnemyCount() > initialCount, "Enemy count should increase after spawning.");
    }
}
