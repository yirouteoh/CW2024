package com.example.demo.levels;

import com.example.demo.screens.LevelView;
import com.example.demo.screens.LevelViewLevelTwo;
import com.example.demo.sounds.SoundManager;
import com.example.demo.actors.plane.BossPlane;

/**
 * Represents the second level of the game.
 * This level features a single boss as the primary challenge.
 */
public class LevelTwo extends LevelParent {

	// Constants
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpeg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final int TARGET_KILL_COUNT = 1;

	private final BossPlane boss;
	private final SoundManager soundManager;

	/**
	 * Constructor for Level Two
	 *
	 * @param screenHeight Screen height
	 * @param screenWidth  Screen width
	 */
	public LevelTwo(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, TARGET_KILL_COUNT);
		this.boss = createBoss();
		this.soundManager = SoundManager.getInstance();
	}

	/**
	 * Creates the boss for Level Two.
	 *
	 * @return A configured {@link BossPlane} instance.
	 */
	private BossPlane createBoss() {
		return new BossPlane();
	}

	/**
	 * Initialize the user's plane and other friendly units
	 */
	@Override
	protected void initializeFriendlyUnits() {
		playLevelMusic();
		addUserPlaneToScene();
	}

	/**
	 * Plays the background music for Level Two.
	 */
	private void playLevelMusic() {
		playLevelMusic(SoundManager.LEVEL_TWO_MUSIC);
	}

	/**
	 * Adds the user's plane to the scene.
	 */
	private void addUserPlaneToScene() {
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Check if the game is over (win or lose conditions)
	 */
	@Override
	protected void checkIfGameOver() {
		if (isGameLost()) {
			handleGameLoss();
		} else if (isBossDefeated()) {
			handleLevelAdvance();
		}
	}

	/**
	 * Determines if the user has lost the game.
	 *
	 * @return true if the user's plane is destroyed, false otherwise.
	 */
	private boolean isGameLost() {
		return userIsDestroyed();
	}

	/**
	 * Handles the loss condition by stopping the level music and transitioning to the loss screen.
	 */
	private void handleGameLoss() {
		stopLevelMusic();
		loseGame();
	}

	/**
	 * Determines if the boss has been defeated.
	 *
	 * @return true if the boss is destroyed, false otherwise.
	 */
	private boolean isBossDefeated() {
		return boss.isDestroyed();
	}

	/**
	 * Handles advancing to the next level by stopping the level music and loading Level Three.
	 */
	private void handleLevelAdvance() {
		stopLevelMusic();
		goToNextLevel(new LevelThree(getScreenHeight(), getScreenWidth()));
	}

	/**
	 * Stops the background music for the level.
	 */
	private void stopLevelMusic() {
		soundManager.stopBackgroundMusic();
	}

	/**
	 * Spawn enemy units (boss for Level Two)
	 */
	@Override
	protected void spawnEnemyUnits() {
		if (shouldSpawnBoss()) {
			spawnBoss();
		}
	}

	/**
	 * Determines if the boss should be spawned.
	 *
	 * @return true if the boss should be spawned, false otherwise.
	 */
	private boolean shouldSpawnBoss() {
		return getCurrentNumberOfEnemies() == 0 && !boss.isDestroyed();
	}

	/**
	 * Spawns the boss and adds its health bar and shield to the scene.
	 */
	private void spawnBoss() {
		addEnemyUnit(boss);
		addBossHealthBarToScene();
		getRoot().getChildren().add(boss.getShieldImage()); // Add the shield image
	}

	/**
	 * Adds the boss's health bar to the scene.
	 */
	private void addBossHealthBarToScene() {
		getRoot().getChildren().addAll(boss.getHealthBarBackground(), boss.getHealthBar());
	}

	/**
	 * Instantiate the level-specific view (UI elements)
	 *
	 * @return LevelView object
	 */
	@Override
	protected LevelView instantiateLevelView() {
		return new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH);
	}
}
