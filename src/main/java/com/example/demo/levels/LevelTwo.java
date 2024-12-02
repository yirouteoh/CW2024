package com.example.demo.levels;

import com.example.demo.screens.LevelView;
import com.example.demo.screens.LevelViewLevelTwo;
import com.example.demo.sounds.SoundManager;
import com.example.demo.actors.plane.Boss;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpeg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final int TARGET_KILL_COUNT = 1; // Set this to the number of kills required for Level 2 to win

	private final Boss boss;
	private SoundManager soundManager;

	/**
	 * Constructor for Level Two
	 *
	 * @param screenHeight Screen height
	 * @param screenWidth  Screen width
	 */
	public LevelTwo(double screenHeight, double screenWidth) {
		// Pass target kill count to LevelParent
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, TARGET_KILL_COUNT);
		this.boss = new Boss();
		this.soundManager = SoundManager.getInstance();

	}

	/**
	 * Initialize the user's plane and other friendly units
	 */
	@Override
	protected void initializeFriendlyUnits() {
		soundManager.playBackgroundMusic(SoundManager.LEVEL_TWO_MUSIC); // Play Level Two music
		getRoot().getChildren().add(getUser());
	}

	/**
	 * Check if the game is over (win or lose conditions)
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			soundManager.stopBackgroundMusic(); // Stop Level Two music
			loseGame();
		} else if (boss.isDestroyed()) {
			soundManager.stopBackgroundMusic(); // Stop Level Two music
			goToNextLevel("com.example.demo.levels.LevelThree");
		}
	}

	/**
	 * Spawn enemy units (boss for Level Two)
	 */
	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0 && !boss.isDestroyed()) {
			addEnemyUnit(boss);
			// Add boss health bar and its background to the scene
			getRoot().getChildren().addAll(boss.getHealthBarBackground(), boss.getHealthBar());
		}
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