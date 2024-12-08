package com.example.demo.levels;

import com.example.demo.screens.LevelView;
import com.example.demo.screens.LevelViewLevelTwo;
import com.example.demo.sounds.SoundManager;
import com.example.demo.actors.plane.Boss;

public class LevelTwo extends LevelParent {

	// Constants
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpeg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final int TARGET_KILL_COUNT = 1;

	private final Boss boss;
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

	// Boss creation encapsulated in its own method for flexibility
	private Boss createBoss() {
		return new Boss();
	}

	/**
	 * Initialize the user's plane and other friendly units
	 */
	@Override
	protected void initializeFriendlyUnits() {
		playLevelMusic();
		addUserPlaneToScene();
	}

	private void playLevelMusic() {
		playLevelMusic(SoundManager.LEVEL_TWO_MUSIC);
	}

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

	private boolean isGameLost() {
		return userIsDestroyed();
	}

	private void handleGameLoss() {
		stopLevelMusic();
		loseGame();
	}

	private boolean isBossDefeated() {
		return boss.isDestroyed();
	}

	private void handleLevelAdvance() {
		stopLevelMusic();
		goToNextLevel(new LevelThree(getScreenHeight(), getScreenWidth()));
	}

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

	private boolean shouldSpawnBoss() {
		return getCurrentNumberOfEnemies() == 0 && !boss.isDestroyed();
	}

	private void spawnBoss() {
		addEnemyUnit(boss);
		addBossHealthBarToScene();
	}

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
