package com.example.demo;

import javafx.scene.image.Image;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final int TARGET_KILL_COUNT = 24; // Total number of kills needed to reach the boss
    private static final double ENEMY_SPAWN_PROBABILITY = 0.25; // Probability of enemy spawn

    private final Boss finalBoss;
    private int currentWave; // Tracks the current wave
    private int totalSpawnedEnemies; // Tracks the total number of spawned enemies
    private final int[] waveEnemyCounts = {5, 8, 11}; // Number of enemies per wave

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, TARGET_KILL_COUNT);

        // Initialize the final boss with submarine.png
        this.finalBoss = new Boss() {
            {
                setImage(new Image(getClass().getResource("/com/example/demo/images/jetplane.png").toExternalForm()));
                setFitHeight(300); // Set the height of the submarine
                setFitWidth(500);  // Set the width of the submarine
            }
        };

        this.currentWave = 0; // Start at wave 0 (first wave)
        this.totalSpawnedEnemies = 0; // No enemies spawned initially
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // User loses the game
        } else if (finalBoss.isDestroyed()) {
            winGame(); // User wins the game after defeating the boss
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        // Stop spawning enemies once totalSpawnedEnemies reaches TARGET_KILL_COUNT
        if (totalSpawnedEnemies < TARGET_KILL_COUNT) {
            spawnWaveEnemies(); // Spawn enemies for the current wave
        } else if (getCurrentNumberOfEnemies() == 0 && !finalBoss.isDestroyed()) {
            addEnemyUnit(finalBoss); // Spawn the final boss after all waves
        }
    }

    private void spawnWaveEnemies() {
        // Get the number of enemies for the current wave
        int enemiesToSpawnInWave = waveEnemyCounts[currentWave];
        int remainingEnemiesInWave = enemiesToSpawnInWave - getCurrentNumberOfEnemies();

        // Spawn only as many enemies as needed for the current wave and total limit
        int remainingEnemiesToSpawn = TARGET_KILL_COUNT - totalSpawnedEnemies;
        int enemiesToSpawn = Math.min(remainingEnemiesInWave, remainingEnemiesToSpawn);

        for (int i = 0; i < enemiesToSpawn; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double newEnemyYPos = Math.random() * getEnemyMaximumYPosition();
                ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyYPos);
                addEnemyUnit(newEnemy);
                totalSpawnedEnemies++; // Increment the total spawned enemy count
            }
        }

        // Move to the next wave if all enemies in the current wave are destroyed
        if (getCurrentNumberOfEnemies() == 0 && totalSpawnedEnemies < TARGET_KILL_COUNT) {
            currentWave++;
        }
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }
}
