package com.example.demo.levels;

import com.example.demo.screens.LevelView;
import com.example.demo.sounds.SoundManager;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.Boss;
import com.example.demo.actors.plane.EnemyPlane;
import com.example.demo.actors.plane.UserPlane;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final int TARGET_KILL_COUNT = 24; // Total number of kills needed to reach the boss
    private static final double ENEMY_SPAWN_PROBABILITY = 0.25; // Probability of enemy spawn
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.02; // 2% chance per frame for power-up spawn

    private final Boss finalBoss;
    private int currentWave; // Tracks the current wave
    private int totalSpawnedEnemies; // Tracks the total number of spawned enemies
    private final int[] waveEnemyCounts = {5, 8, 11}; // Number of enemies per wave
    private SoundManager soundManager;

    private boolean finalBossMessageDisplayed = false; // Tracks if the message is displayed
    private boolean finalBossSpawned = false; // Tracks if the boss has been spawned

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, TARGET_KILL_COUNT);

        // Initialize the final boss with jetplane.png
        this.finalBoss = new Boss() {
            {
                setImage(new Image(getClass().getResource("/com/example/demo/images/jetplane.png").toExternalForm()));
                setFitHeight(300); // Set the height of the boss
                setFitWidth(500);  // Set the width of the boss
            }
        };

        this.currentWave = 0; // Start at wave 0 (first wave)
        this.totalSpawnedEnemies = 0; // No enemies spawned initially
        this.soundManager = SoundManager.getInstance();
    }

    @Override
    protected void initializeFriendlyUnits() {
        soundManager.playBackgroundMusic(SoundManager.LEVEL_THREE_MUSIC); // Play Level Three music
        UserPlane user = getUser(); // Assuming getUser() creates or retrieves the UserPlane
        user.setLevelParent(this); // Pass the current LevelParent instance to the user plane
        getRoot().getChildren().add(user);
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            soundManager.stopBackgroundMusic(); // Stop Level Three music
            loseGame(); // User loses the game
        } else if (finalBoss.isDestroyed()) {
            soundManager.stopBackgroundMusic(); // Stop Level Three music
            winGame(); // User wins the game after defeating the boss
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        // Stop spawning enemies once totalSpawnedEnemies reaches TARGET_KILL_COUNT
        if (totalSpawnedEnemies < TARGET_KILL_COUNT) {
            spawnWaveEnemies(); // Spawn enemies for the current wave
        } else if (getCurrentNumberOfEnemies() == 0 && !finalBossSpawned) {
            if (!finalBossMessageDisplayed) {
                showFinalBossMessage(); // Display the message
                finalBossMessageDisplayed = true; // Mark the message as displayed
            }
        }

        // Randomly spawn spreadshot power-up
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
            double screenWidthLimit = getScreenWidth() / 2; // Limit to the left half of the screen
            double x = Math.random() * screenWidthLimit; // Generate an x-coordinate within the left half
            addPowerUp(new SpreadshotPowerUp(x, 0)); // Spawn the spreadshot power-up
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

    private void showFinalBossMessage() {
        // Create the message text
        Text bossMessage = new Text("The Final Boss is Entering!");
        bossMessage.setFont(Font.font("Orbitron", javafx.scene.text.FontWeight.EXTRA_BOLD, 70)); // Fancy font
        bossMessage.setFill(Color.WHITE); // White font color
        bossMessage.setStroke(Color.RED); // Outer stroke color
        bossMessage.setStrokeWidth(2); // Outline thickness
        bossMessage.setEffect(new javafx.scene.effect.Glow(0.7)); // Add glow effect
        bossMessage.setX(getScreenWidth() / 2 - 400); // Adjust horizontal centering
        bossMessage.setY(getScreenHeight() / 2); // Center vertically

        // Add the text to the root node
        getRoot().getChildren().add(bossMessage);

        // Add fade-in, smaller zoom, and fade-out effects
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(Duration.seconds(1), bossMessage);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Smaller zoom-in effect
        javafx.animation.ScaleTransition zoomIn = new javafx.animation.ScaleTransition(Duration.seconds(1), bossMessage);
        zoomIn.setFromX(0.9); // Smaller starting size
        zoomIn.setFromY(0.9); // Smaller starting size
        zoomIn.setToX(1.1); // Less dramatic zoom-in
        zoomIn.setToY(1.1); // Less dramatic zoom-in

        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(Duration.seconds(1), bossMessage);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        javafx.animation.SequentialTransition sequence = new javafx.animation.SequentialTransition(fadeIn, zoomIn, fadeOut);
        sequence.setOnFinished(event -> {
            getRoot().getChildren().remove(bossMessage); // Remove the message
            spawnFinalBoss(); // Spawn the boss
        });
        sequence.play();
    }





    private void spawnFinalBoss() {
        addEnemyUnit(finalBoss); // Add the final boss
        finalBossSpawned = true; // Mark the boss as spawned

        // Add boss health bar and its background to the scene
        getRoot().getChildren().addAll(finalBoss.getHealthBarBackground(), finalBoss.getHealthBar());
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

}
