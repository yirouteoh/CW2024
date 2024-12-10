package com.example.demo.levels;

import com.example.demo.screens.LevelView;
import com.example.demo.sounds.SoundManager;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.EnemyPlane;

/**
 * Represents the first level of the game.
 * <p>
 * In this level, the player must defeat a set number of enemies to advance to the next level.
 * </p>
 */
public class LevelOne extends LevelParent {

	// Constants
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	private static final double ENEMY_SPAWN_PROBABILITY = 0.20;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	private final SoundManager soundManager;

	/**
	 * Constructor for Level One
	 *
	 * @param screenHeight Screen height
	 * @param screenWidth  Screen width
	 */
	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, KILLS_TO_ADVANCE);
		this.soundManager = SoundManager.getInstance();
	}

	/**
	 * Check if the game is over (user loses or advances to the next level)
	 */
	@Override
	protected void checkIfGameOver() {
		if (isGameLost()) {
			handleGameLoss();
		} else if (isKillTargetReached()) {
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
	 * Handles the game loss scenario by stopping the background music and transitioning to the loss screen.
	 */
	private void handleGameLoss() {
		soundManager.stopBackgroundMusic();
		loseGame();
	}

	/**
	 * Determines if the kill target for advancing the level has been reached.
	 *
	 * @return true if the kill target is reached, false otherwise.
	 */
	private boolean isKillTargetReached() {
		return getKillCountDisplay().getKillCount() >= KILLS_TO_ADVANCE;
	}

	/**
	 * Handles advancing to the next level by stopping the background music and loading LevelTwo.
	 */
	private void handleLevelAdvance() {
		soundManager.stopBackgroundMusic();
		goToNextLevel(new LevelTwo(getScreenHeight(), getScreenWidth()));
	}

	/**
	 * Initialize the user's plane and other friendly units
	 */
	@Override
	protected void initializeFriendlyUnits() {
		playLevelMusic();
		addUserToScene();
	}

	/**
	 * Plays the background music for Level One.
	 */
	private void playLevelMusic() {
		playLevelMusic(SoundManager.LEVEL_ONE_MUSIC);
	}

	/**
	 * Adds the user's plane to the scene if it is not already present.
	 */
	private void addUserToScene() {
		if (!getRoot().getChildren().contains(getUser())) { // Check if already added
			getRoot().getChildren().add(getUser());
		}
	}

	/**
	 * Spawn enemy units based on the spawn probability and current number of enemies
	 */
	@Override
	protected void spawnEnemyUnits() {
		int enemiesToSpawn = TOTAL_ENEMIES - getCurrentNumberOfEnemies();
		for (int i = 0; i < enemiesToSpawn; i++) {
			if (shouldSpawnEnemy()) {
				spawnEnemy();
			}
		}
	}

	/**
	 * Determines if an enemy should be spawned in the current frame based on the spawn probability.
	 *
	 * @return true if an enemy should be spawned, false otherwise.
	 */
	private boolean shouldSpawnEnemy() {
		return Math.random() < ENEMY_SPAWN_PROBABILITY;
	}

	/**
	 * Spawns a new enemy plane and adds it to the level.
	 */
	private void spawnEnemy() {
		double initialYPosition = Math.random() * getEnemyMaximumYPosition();
		ActiveActorDestructible newEnemy = createEnemy(initialYPosition);
		addEnemyUnit(newEnemy);
	}

	/**
	 * Creates a new enemy plane at the specified vertical position.
	 *
	 * @param initialYPosition The initial Y position of the enemy plane.
	 * @return A new {@link ActiveActorDestructible} representing the enemy plane.
	 */
	private ActiveActorDestructible createEnemy(double initialYPosition) {
		return new EnemyPlane(getScreenWidth(), initialYPosition);
	}

	/**
	 * Instantiate the level-specific view (UI elements like health display)
	 *
	 * @return LevelView object
	 */
	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
	}
}
