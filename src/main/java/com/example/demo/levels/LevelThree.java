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

/**
 * Represents the third level of the game. This level introduces waves of enemies
 * and culminates in a final boss battle. Power-ups are also introduced in this level.
 */
public class LevelThree extends LevelParent {

    // Constants
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final int TARGET_KILL_COUNT = 24; // Total kills needed before boss
    private static final double ENEMY_SPAWN_PROBABILITY = 0.25; // Probability of spawning enemies
    private static final double POWER_UP_SPAWN_PROBABILITY = 0.02; // Probability of spawning power-ups
    private static final String FINAL_BOSS_IMAGE = "/com/example/demo/images/jetplane.png";

    private final Boss finalBoss; // The final boss of the level
    private final SoundManager soundManager; // Handles background music and sounds
    private javafx.animation.SequentialTransition finalBossMessageTimeline;
    private int currentWave; // Current wave number
    private int totalSpawnedEnemies; // Total number of spawned enemies
    private final int[] waveEnemyCounts = {5, 8, 11}; // Number of enemies per wave
    private boolean finalBossMessageDisplayed = false; // Tracks if boss entry message is displayed
    private boolean finalBossSpawned = false; // Tracks if the final boss has been spawned

    /**
     * Constructs the LevelThree instance.
     *
     * @param screenHeight the height of the screen
     * @param screenWidth  the width of the screen
     */
    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, TARGET_KILL_COUNT);
        this.finalBoss = createFinalBoss();
        this.soundManager = SoundManager.getInstance();
        this.currentWave = 0;
        this.totalSpawnedEnemies = 0;
    }

    /**
     * Creates and configures the final boss for the level.
     *
     * @return a configured Boss instance
     */
    private Boss createFinalBoss() {
        Boss boss = new Boss();
        boss.setImage(new Image(getClass().getResource(FINAL_BOSS_IMAGE).toExternalForm()));
        boss.setFitHeight(300);
        boss.setFitWidth(500);
        return boss;
    }

    /**
     * Initializes the user's plane and other friendly units for the level.
     */
    @Override
    protected void initializeFriendlyUnits() {
        playLevelMusic(SoundManager.LEVEL_THREE_MUSIC);
        addUserToScene();
    }

    /**
     * Adds the user-controlled plane to the scene.
     */
    private void addUserToScene() {
        UserPlane user = getUser();
        user.setLevelParent(this);
        getRoot().getChildren().add(user);
    }

    /**
     * Checks if the game is over, either by user loss or defeating the boss.
     */
    @Override
    protected void checkIfGameOver() {
        if (isGameLost()) {
            handleGameLoss();
        } else if (isFinalBossDefeated()) {
            handleGameWin();
        }
    }

    /**
     * Determines if the user has lost the game.
     *
     * @return true if the user is destroyed, false otherwise
     */
    private boolean isGameLost() {
        return userIsDestroyed();
    }

    /**
     * Handles game loss conditions by stopping the music and transitioning to the game over screen.
     */
    private void handleGameLoss() {
        soundManager.stopBackgroundMusic();
        loseGame();
    }

    /**
     * Determines if the final boss has been defeated.
     *
     * @return true if the boss is destroyed, false otherwise
     */
    private boolean isFinalBossDefeated() {
        return finalBoss.isDestroyed();
    }

    /**
     * Handles game win conditions by stopping the music and transitioning to the win screen.
     */
    private void handleGameWin() {
        soundManager.stopBackgroundMusic();
        winGame();
    }

    /**
     * Spawns enemy units and power-ups in the level. Spawns the boss once all waves are cleared.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (totalSpawnedEnemies < TARGET_KILL_COUNT) {
            spawnWaveEnemies();
        } else if (getCurrentNumberOfEnemies() == 0 && !finalBossSpawned) {
            displayFinalBossMessage();
        }

        spawnPowerUpsRandomly();
    }

    /**
     * Spawns enemies for the current wave.
     */
    private void spawnWaveEnemies() {
        int remainingEnemiesToSpawn = getRemainingEnemiesToSpawn();

        for (int i = 0; i < remainingEnemiesToSpawn; i++) {
            if (shouldSpawnEnemy()) {
                spawnEnemy();
            }
        }

        moveToNextWaveIfComplete();
    }

    /**
     * Determines how many enemies should be spawned for the current wave.
     *
     * @return the number of enemies to spawn
     */
    private int getRemainingEnemiesToSpawn() {
        int enemiesInCurrentWave = waveEnemyCounts[currentWave];
        int remainingEnemiesInWave = enemiesInCurrentWave - getCurrentNumberOfEnemies();
        int remainingOverallEnemies = TARGET_KILL_COUNT - totalSpawnedEnemies;

        return Math.min(remainingEnemiesInWave, remainingOverallEnemies);
    }

    /**
     * Determines whether to spawn an enemy based on a random chance.
     *
     * @return true if an enemy should be spawned, false otherwise
     */
    private boolean shouldSpawnEnemy() {
        return Math.random() < ENEMY_SPAWN_PROBABILITY;
    }

    /**
     * Spawns a single enemy plane at a random vertical position.
     */
    private void spawnEnemy() {
        double yPosition = Math.random() * getEnemyMaximumYPosition();
        ActiveActorDestructible enemy = new EnemyPlane(getScreenWidth(), yPosition);
        addEnemyUnit(enemy);
        totalSpawnedEnemies++;
    }

    /**
     * Advances to the next wave if all enemies in the current wave are cleared.
     */
    private void moveToNextWaveIfComplete() {
        if (getCurrentNumberOfEnemies() == 0 && totalSpawnedEnemies < TARGET_KILL_COUNT) {
            currentWave++;
        }
    }

    /**
     * Spawns power-ups randomly during the level.
     */
    private void spawnPowerUpsRandomly() {
        if (Math.random() < POWER_UP_SPAWN_PROBABILITY) {
            spawnSpreadshotPowerUp();
        }
    }

    /**
     * Spawns a spreadshot power-up at a random position within the left half of the screen.
     */
    private void spawnSpreadshotPowerUp() {
        double x = Math.random() * (getScreenWidth() / 2);
        addPowerUp(new SpreadshotPowerUp(x, 0));
    }

    /**
     * Displays a message announcing the final boss and spawns the boss.
     */
    private void displayFinalBossMessage() {
        if (!finalBossMessageDisplayed) {
            showFinalBossMessage();
            finalBossMessageDisplayed = true;
        }
    }

    /**
     * Creates and displays the final boss entry message with animations.
     */
    private void showFinalBossMessage() {
        Text bossMessage = createFinalBossMessageText();
        getRoot().getChildren().add(bossMessage);

        finalBossMessageTimeline = createFinalBossMessageAnimation(bossMessage);
        finalBossMessageTimeline.setOnFinished(event -> {
            getRoot().getChildren().remove(bossMessage);
            spawnFinalBoss();
        });

        finalBossMessageTimeline.play();
    }



    public void pauseFinalBossMessage() {
        if (finalBossMessageTimeline != null && finalBossMessageTimeline.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            finalBossMessageTimeline.pause();
        }
    }

    public void resumeFinalBossMessage() {
        if (finalBossMessageTimeline != null && finalBossMessageTimeline.getStatus() == javafx.animation.Animation.Status.PAUSED) {
            finalBossMessageTimeline.play();
        }
    }



    /**
     * Creates the text for the final boss entry message.
     *
     * @return a configured Text object
     */
    private Text createFinalBossMessageText() {
        Text bossMessage = new Text("The Final Boss is Entering!");
        bossMessage.setFont(Font.font("Orbitron", javafx.scene.text.FontWeight.EXTRA_BOLD, 70));
        bossMessage.setFill(Color.WHITE);
        bossMessage.setStroke(Color.RED);
        bossMessage.setStrokeWidth(2);
        bossMessage.setEffect(new javafx.scene.effect.Glow(0.7));
        bossMessage.setX(getScreenWidth() / 2 - 400);
        bossMessage.setY(getScreenHeight() / 2);
        return bossMessage;
    }

    /**
     * Creates the animation for the final boss entry message.
     *
     * @param bossMessage the Text object for the message
     * @return a configured SequentialTransition object
     */
    private javafx.animation.SequentialTransition createFinalBossMessageAnimation(Text bossMessage) {
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(Duration.seconds(1), bossMessage);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        javafx.animation.ScaleTransition zoomIn = new javafx.animation.ScaleTransition(Duration.seconds(1), bossMessage);
        zoomIn.setFromX(0.9);
        zoomIn.setFromY(0.9);
        zoomIn.setToX(1.1);
        zoomIn.setToY(1.1);

        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(Duration.seconds(1), bossMessage);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        return new javafx.animation.SequentialTransition(fadeIn, zoomIn, fadeOut);
    }

    /**
     * Spawns the final boss and adds its health bar to the scene.
     */
    private void spawnFinalBoss() {
        addEnemyUnit(finalBoss);
        finalBossSpawned = true;
        addFinalBossHealthBarToScene();
        getRoot().getChildren().add(finalBoss.getShieldImage()); // Add the shield image
    }

    /**
     * Adds the health bar of the final boss to the scene.
     */
    private void addFinalBossHealthBarToScene() {
        getRoot().getChildren().addAll(finalBoss.getHealthBarBackground(), finalBoss.getHealthBar());
    }

    /**
     * Instantiates the level-specific UI components.
     *
     * @return a LevelView object
     */
    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }
}
