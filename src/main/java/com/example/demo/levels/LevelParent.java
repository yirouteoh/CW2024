package com.example.demo.levels;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.factory.BasicFighterPlaneFactory;
import com.example.demo.actors.plane.UserPlane;
import com.example.demo.screens.*;
import com.example.demo.sounds.SoundManager;
import com.example.demo.utils.KillCountDisplay;
import com.example.demo.managers.ActorManager;
import com.example.demo.managers.GameLoopManager;
import com.example.demo.managers.GameStateManager;
import com.example.demo.managers.CollisionManager;
import com.example.demo.managers.InputManager;
import com.example.demo.managers.PauseManager;
import com.example.demo.managers.SceneManager;
import com.example.demo.managers.EnemyManager;
import com.example.demo.managers.EventHandler;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;


public abstract class LevelParent {

	// ==================== Constants & Core Properties =====================
	// Constants and core attributes shared across all levels.

	// Constants
	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;

	// Core Properties
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final UserPlane user;
	private final SceneManager sceneManager;
	private final KillCountDisplay killCountDisplay;

	private final ActorManager actorManager = new ActorManager();
	private GameLoopManager gameLoopManager;
	private final GameStateManager gameStateManager = new GameStateManager();
	private CollisionManager collisionManager;
	private InputManager inputManager;
	private PauseManager pauseManager;
	private EnemyManager enemyManager;
	private SoundManager soundManager;
	private EventHandler eventHandler;

	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	private int currentNumberOfEnemies;
	private LevelView levelView;
	private CountdownOverlay countdownOverlay;
	private boolean countdownInProgress = false; // Flag to track countdown state

	// ==================== Initialization =====================
	// Methods for setting up the game environment, including the background, timeline, and handlers.

	/**
	 * Constructs a base class for levels in the game.
	 *
	 * @param backgroundImageName Path to the background image resource for the level.
	 * @param screenHeight        Height of the game window.
	 * @param screenWidth         Width of the game window.
	 * @param playerInitialHealth The starting health of the player's plane.
	 * @param targetKillCount     The number of kills required to complete the level.
	 */

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, int targetKillCount) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;

		this.gameLoopManager = new GameLoopManager(Duration.millis(MILLISECOND_DELAY), this::updateScene);
		this.soundManager = SoundManager.getInstance();

		// Pass 'this' (current LevelParent instance)
		this.sceneManager = new SceneManager(backgroundImageName, screenHeight, screenWidth,
				gameLoopManager, soundManager, null, this);

		this.pauseManager = new PauseManager(gameLoopManager, gameStateManager, soundManager, sceneManager.getRoot());
		this.sceneManager.setPauseManager(pauseManager);

		this.user = new UserPlane(playerInitialHealth);
		this.user.setLevelParent(this);
		actorManager.addFriendlyUnit(user, sceneManager.getRoot());

		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;

		this.killCountDisplay = new KillCountDisplay(600, 55, targetKillCount);
		this.countdownOverlay = new CountdownOverlay(sceneManager.getRoot(), screenWidth, screenHeight);
		this.collisionManager = new CollisionManager(actorManager, soundManager, user, sceneManager.getRoot(), screenWidth);
		this.inputManager = new InputManager(user, this, soundManager);
		this.enemyManager = new EnemyManager(
				actorManager,
				sceneManager.getRoot(),
				new BasicFighterPlaneFactory(), // Factory
				screenWidth,                    // Pass screenWidth
				enemyMaximumYPosition,          // Pass enemyMaximumYPosition
				0.25,                           // Spawn probability
				10                              // Max enemies
		);
		this.eventHandler = new EventHandler(gameStateManager, soundManager, pauseManager, this);

	}

	/**
	 * Initializes the timeline that controls the game loop.
	 * The timeline runs indefinitely and invokes the updateScene method at fixed intervals.
	 */
	protected abstract void initializeFriendlyUnits();
	protected abstract void checkIfGameOver();

	protected void spawnEnemyUnits() {
		enemyManager.spawnEnemies();
	}

	protected abstract LevelView instantiateLevelView();

	/**
	 * Configures and initializes the game scene, including the background, friendly units, and input handlers.
	 *
	 * @return The fully configured game scene.
	 */
	public Scene initializeScene() {
		Scene scene = sceneManager.initializeScene();
		initializeFriendlyUnits(); // Add the user plane and other units
		levelView.showHeartDisplay(); // Show health or level-related UI
		sceneManager.getRoot().getChildren().add(killCountDisplay.getDisplay()); // Add the kill count display to the root
		inputManager.initializeInputHandlers(scene);
		sceneManager.addPauseButton(this::pauseGame); // Add pause button
		return scene;
	}


	public void startGame(Stage primaryStage) {
		Scene scene = initializeScene();
		primaryStage.setScene(scene); // Set the Scene on the Stage
		primaryStage.show();

		countdownInProgress = true; // Lock input during countdown

		countdownOverlay.showCountdown(() -> {
			countdownInProgress = false;
			gameStateManager.changeState(GameStateManager.GameState.PLAYING);
			gameLoopManager.start();
		});
	}

	protected void playLevelMusic(String musicFilePath) {
		if (!soundManager.isBackgroundMusicMuted()) {
			soundManager.playBackgroundMusic(musicFilePath);
		}
	}


	// ==================== Game Loop =====================
	// Methods that define and update the game loop, including actor updates, collisions, and UI refresh.

	/**
	 * The primary game loop method. Updates game logic, handles collisions, cleans up destroyed actors,
	 * updates UI elements, and checks game-over conditions.
	 */

	private void updateScene() {
		updateEnemyUnits();
		actorManager.updateActors();
		collisionManager.handleAllCollisions();
		actorManager.cleanUpDestroyedActors(sceneManager.getRoot());
		updateUIElements();
		checkIfGameOver();
	}

	/**
	 * Updates enemy-related logic, such as spawning and fire generation.
	 */
	private void updateEnemyUnits() {
		spawnEnemyUnits();
		generateEnemyFire();
		updateNumberOfEnemies();
	}

	/**
	 * Updates UI elements like kill counts and level view.
	 */
	private void updateUIElements() {
		updateKillCount();
		updateLevelView();
	}

	private void generateEnemyFire() {
		enemyManager.generateEnemyFire();
	}


	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyManager.getCurrentEnemyCount();
	}


	private void updateKillCount() {
		int kills = currentNumberOfEnemies - actorManager.getEnemyUnits().size();
		for (int i = 0; i < kills; i++) {
			killCountDisplay.incrementKillCount();
			System.out.println("Enemy destroyed! Total kills: " + killCountDisplay.getKillCount());
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	public void goToNextLevel(LevelParent nextLevel) {
		try {
			gameLoopManager.stop();
			sceneManager.getRoot().getChildren().clear(); // Clear the current root to avoid duplicates
			Scene nextScene = nextLevel.initializeScene();
			if (nextScene == null) { // Null check for safety
				showErrorDialog("Failed to load the next level: Scene is null.");
				return;
			}
			Stage primaryStage = (Stage) sceneManager.getRoot().getScene().getWindow();
			primaryStage.setScene(nextScene);
			nextLevel.startGame(primaryStage); // Pass the Stage to startGame
		} catch (Exception e) {
			showErrorDialog("Error transitioning to next level: " + e.getMessage());
		}
	}

	public void showErrorDialog(String message) {
		sceneManager.showErrorDialog(message);
	}

	protected KillCountDisplay getKillCountDisplay() {
		return killCountDisplay;
	}


	public void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null) {
			actorManager.addUserProjectile(projectile, sceneManager.getRoot());
		}
	}

	public void addProjectile(ActiveActorDestructible projectile) {
		actorManager.addUserProjectile(projectile, sceneManager.getRoot()); // Use ActorManager to add the projectile
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	protected int getCurrentNumberOfEnemies() {
		return actorManager.getEnemyUnits().size();
	}


	// ==================== Pause & Game State Management =====================
	// Methods for pausing, resuming, and managing game-over conditions.

	/**
	 * Pauses the game and displays the pause menu.
	 */

	public void showPauseScreen() {
		pauseManager.showPauseScreen(this);
	}

	protected void pauseGame() {
		// Pause the game loop
		gameLoopManager.pause();

		// Show the pause menu using PauseManager
		pauseManager.showPauseScreen(this);
	}

	protected void winGame() {
		eventHandler.handleWin();
	}

	protected void loseGame() {
		eventHandler.handleLose();
	}

	// ==================== Actor & Projectile Management =====================
	// Methods responsible for adding, removing, or interacting with actors and projectiles.

	public void addEnemyUnit(ActiveActorDestructible enemy) {
		actorManager.addEnemyUnit(enemy, sceneManager.getRoot()); // Use ActorManager to add the enemy
	}

	public void addPowerUp(ActiveActorDestructible powerUp) {
		actorManager.addPowerUp(powerUp, sceneManager.getRoot()); // Use ActorManager to add the power-up
	}

	// ==================== Utility Methods =====================
	// Miscellaneous methods, including getters and utility functions for game logic.

	// Getter for GameStateManager
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}

	// Getter for GameLoopManager
	public GameLoopManager getGameLoopManager() {
		return gameLoopManager;
	}

	// Getter for countdownInProgress
	public boolean isCountdownInProgress() {
		return countdownInProgress;
	}

	protected UserPlane getUser() {
		return user;
	}

	public Group getRoot() {
		return sceneManager.getRoot();
	}

	public Scene getScene() {
		return sceneManager.getScene();
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	public double getScreenWidth() {
		return screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}


	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

}