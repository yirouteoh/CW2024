package com.example.demo.levels;

import com.example.demo.screens.LevelView;
import com.example.demo.sounds.SoundManager;
import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.EnemyPlane;

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

	private boolean isGameLost() {
		return userIsDestroyed();
	}

	private void handleGameLoss() {
		soundManager.stopBackgroundMusic();
		loseGame();
	}

	private boolean isKillTargetReached() {
		return getKillCountDisplay().getKillCount() >= KILLS_TO_ADVANCE;
	}

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

	private void playLevelMusic() {
		soundManager.playBackgroundMusic(SoundManager.LEVEL_ONE_MUSIC);
	}

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

	private boolean shouldSpawnEnemy() {
		return Math.random() < ENEMY_SPAWN_PROBABILITY;
	}

	private void spawnEnemy() {
		double initialYPosition = Math.random() * getEnemyMaximumYPosition();
		ActiveActorDestructible newEnemy = createEnemy(initialYPosition);
		addEnemyUnit(newEnemy);
	}

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
