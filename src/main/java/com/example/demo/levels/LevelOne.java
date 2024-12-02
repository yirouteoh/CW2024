package com.example.demo.levels;

import com.example.demo.screens.LevelView;
import com.example.demo.sounds.SoundManager;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.EnemyPlane;

public class LevelOne extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.levels.LevelTwo";
	private static final int TOTAL_ENEMIES = 5; // Max number of enemies on screen at a time
	private static final int KILLS_TO_ADVANCE = 10; // Number of kills required to advance
	private static final double ENEMY_SPAWN_PROBABILITY = 0.20; // Probability of spawning a new enemy
	private static final int PLAYER_INITIAL_HEALTH = 5; // User plane initial health

	private SoundManager soundManager;

	/**
	 * Constructor for Level One
	 *
	 * @param screenHeight Screen height
	 * @param screenWidth  Screen width
	 */
	public LevelOne(double screenHeight, double screenWidth) {
		// Pass the target kills (KILLS_TO_ADVANCE) to the parent constructor
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, KILLS_TO_ADVANCE);
		this.soundManager = SoundManager.getInstance();

	}

	/**
	 * Check if the game is over (user loses or advances to the next level)
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			soundManager.stopBackgroundMusic(); // Stop music on game over
			loseGame(); // Trigger loss condition
		} else if (userHasReachedKillTarget()) {
			soundManager.stopBackgroundMusic(); // Stop music before transitioning
			goToNextLevel(NEXT_LEVEL); // Advance to Level Two
		}
	}

	/**
	 * Initialize the user's plane and other friendly units
	 */
	@Override
	protected void initializeFriendlyUnits() {
		soundManager.playBackgroundMusic(SoundManager.LEVEL_ONE_MUSIC); // Play Level One music
		getRoot().getChildren().add(getUser()); // Add the user's plane to the scene
	}

	/**
	 * Spawn enemy units based on the spawn probability and current number of enemies
	 */
	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
			if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
				// Spawn an enemy at a random vertical position
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
				addEnemyUnit(newEnemy); // Add the enemy to the game
			}
		}
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

	/**
	 * Check if the user has reached the target kill count
	 *
	 * @return True if the user has reached the target kill count
	 */
	private boolean userHasReachedKillTarget() {
		return getKillCountDisplay().getKillCount() >= KILLS_TO_ADVANCE;
	}
}
