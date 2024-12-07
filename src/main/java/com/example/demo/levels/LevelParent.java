package com.example.demo.levels;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.FighterPlane;
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

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Platform;

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

	private final Group root;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;
	private final KillCountDisplay killCountDisplay;

	private final ActorManager actorManager = new ActorManager();
	private GameLoopManager gameLoopManager;
	private final GameStateManager gameStateManager = new GameStateManager();
	private CollisionManager collisionManager;
	private InputManager inputManager;
	private PauseManager pauseManager;

	private ImageView pauseButton; // Pause button ImageView
	private SoundManager soundManager;

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
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.gameLoopManager = new GameLoopManager(Duration.millis(MILLISECOND_DELAY), this::updateScene);
		this.user = new UserPlane(playerInitialHealth);
		this.user.setLevelParent(this);
		actorManager.addFriendlyUnit(user, root);

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;

		// Initialize KillCountDisplay with the target kill count
		this.killCountDisplay = new KillCountDisplay(600, 55, targetKillCount);
		this.soundManager = SoundManager.getInstance();
		this.countdownOverlay = new CountdownOverlay(root, screenWidth, screenHeight);
		this.collisionManager = new CollisionManager(actorManager, soundManager, user, root, screenWidth);
		this.inputManager = new InputManager(user, this, soundManager);
		this.pauseManager = new PauseManager(gameLoopManager, gameStateManager, soundManager, root);

	}

	/**
	 * Initializes the timeline that controls the game loop.
	 * The timeline runs indefinitely and invokes the `updateScene` method at fixed intervals.
	 */

	protected abstract void initializeFriendlyUnits();
	protected abstract void checkIfGameOver();
	protected abstract void spawnEnemyUnits();
	protected abstract LevelView instantiateLevelView();

	/**
	 * Configures and initializes the game scene, including the background, friendly units, and input handlers.
	 *
	 * @return The fully configured game scene.
	 */
	public Scene initializeScene() {
		root.getChildren().clear(); // Clear all children from the root before initializing
		initializeBackground(); // Set up the background and add to root
		initializeFriendlyUnits(); // Add the user plane and other units
		levelView.showHeartDisplay(); // Show health or level-related UI
		root.getChildren().add(killCountDisplay.getDisplay()); // Add the kill count display to the root
		inputManager.initializeInputHandlers(scene);
		return scene; // Return the configured scene
	}


	/**
	 * Configures the level background, including its dimensions and position in the scene graph.
	 * Also adds the pause button to the scene.
	 */
	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);

		root.getChildren().add(background);

		// Add the pause button
		addPauseButton();
	}

	protected KillCountDisplay getKillCountDisplay() {
		return killCountDisplay;
	}


	public void startGame() {
		countdownInProgress = true; // Disable input during countdown
		background.requestFocus(); // Ensure the background receives focus for key events

		// Use CountdownOverlay to show a 3-2-1-GO sequence
		CountdownOverlay countdownOverlay = new CountdownOverlay(root, screenWidth, screenHeight);
		countdownOverlay.showCountdown(() -> {
			countdownInProgress = false; // Re-enable input after countdown
			gameStateManager.changeState(GameStateManager.GameState.PLAYING); // Update state to PLAYING
			gameLoopManager.start(); // Start the game loop
		});
	}



	// ==================== Game Loop =====================
	// Methods that define and update the game loop, including actor updates, collisions, and UI refresh.

	/**
	 * The primary game loop method. Updates game logic, handles collisions, cleans up destroyed actors,
	 * updates UI elements, and checks game-over conditions.
	 */

	private void updateScene() {
		updateEnemyUnits();
		updateActors();
		handleAllCollisions();
		cleanUpDestroyedActors();
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
	 * Updates all actors in the game, including their movement and actions.
	 */
	private void updateActors() {
		actorManager.updateActors();
	}

	/**
	 * Handles all collision-related logic.
	 */
	private void handleAllCollisions() {
		collisionManager.handleAllCollisions();
	}

	/**
	 * Cleans up destroyed actors from the game.
	 */
	private void cleanUpDestroyedActors() {
		actorManager.cleanUpDestroyedActors(root);
	}

	/**
	 * Updates UI elements like kill counts and level view.
	 */
	private void updateUIElements() {
		updateKillCount();
		updateLevelView();
	}

	private void generateEnemyFire() {
		actorManager.getEnemyUnits().forEach(enemy -> {
			if (enemy instanceof FighterPlane fighter) {
				ActiveActorDestructible projectile = fighter.fireProjectile();
				if (projectile != null) {
					actorManager.addEnemyProjectile(projectile, root);
				}
			}
		});
	}


	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = actorManager.getEnemyUnits().size();
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
			root.getChildren().clear(); // Clear the current root to avoid duplicates
			Scene nextScene = nextLevel.initializeScene();
			if (nextScene == null) { // Null check for safety
				showErrorDialog("Failed to load the next level: Scene is null.");
				return;
			}
			Stage primaryStage = (Stage) root.getScene().getWindow();
			primaryStage.setScene(nextScene);
			nextLevel.startGame(); // Start the next level
		} catch (Exception e) {
			showErrorDialog("Error transitioning to next level: " + e.getMessage());
		}
	}



	public void showErrorDialog(String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.show(); // Use non-blocking show()
		});
	}


	public void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null) {
			actorManager.addUserProjectile(projectile, root);
		}
	}

	public void addProjectile(ActiveActorDestructible projectile) {
		actorManager.addUserProjectile(projectile, root); // Use ActorManager to add the projectile
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}


	// ==================== Pause & Game State Management =====================
	// Methods for pausing, resuming, and managing game-over conditions.

	/**
	 * Pauses the game and displays the pause menu.
	 */

	private void addPauseButton() {
		Image pauseImage = new Image(getClass().getResource("/com/example/demo/images/pause.png").toExternalForm());
		pauseButton = new ImageView(pauseImage);

		pauseButton.setFitWidth(50);
		pauseButton.setFitHeight(50);
		pauseButton.setX(screenWidth - 70);
		pauseButton.setY(20);

		pauseButton.setPickOnBounds(true);
		pauseButton.setFocusTraversable(true);
		pauseButton.setMouseTransparent(false);

		pauseButton.setOnMouseClicked(event -> {
			gameLoopManager.pause();
			soundManager.pauseBackgroundMusic(); // Pause music
			showPauseScreen();
		});

		root.getChildren().add(pauseButton);
	}

	public void showPauseScreen() {
		pauseManager.showPauseScreen(this);
	}


	private void restartGame() {
		pauseManager.restartGame(this);
	}


	private void returnToMainMenu() {
		pauseManager.returnToMainMenu(this);
	}

	protected void winGame() {
		if (!gameStateManager.isWin()) { // Prevent duplicate calls
			gameLoopManager.stop();
			soundManager.stopBackgroundMusic();
			soundManager.playBackgroundMusic(SoundManager.WIN_GAME_MUSIC); // Play the win music
			gameStateManager.changeState(GameStateManager.GameState.WIN); // Update state to WIN

			// Add the enhanced WinImage to the root
			WinImage winScreen = new WinImage(
					screenWidth,
					screenHeight,
					this::restartGame,       // Restart callback
					this::returnToMainMenu   // Exit callback
			);
			root.getChildren().add(winScreen);
		}
	}

	protected void loseGame() {
		if (!gameStateManager.isGameOver()) { // Prevent duplicate calls
			gameLoopManager.stop();
			soundManager.stopBackgroundMusic();
			soundManager.playBackgroundMusic(SoundManager.GAME_OVER_MUSIC); // Play the Game Over music
			gameStateManager.changeState(GameStateManager.GameState.GAME_OVER); // Update state to GAME_OVER

			// Add the GameOverImage to the root
			GameOverImage gameOverImage = new GameOverImage(
					screenWidth,
					screenHeight,
					this::restartGame,       // Pass the restart callback
					this::returnToMainMenu   // Pass the exit callback
			);
			root.getChildren().add(gameOverImage);
		}
	}



	protected int getCurrentNumberOfEnemies() {
		return actorManager.getEnemyUnits().size();
	}

	// ==================== Actor & Projectile Management =====================
	// Methods responsible for adding, removing, or interacting with actors and projectiles.

	public void addEnemyUnit(ActiveActorDestructible enemy) {
		actorManager.addEnemyUnit(enemy, root); // Use ActorManager to add the enemy
	}

	public void addPowerUp(ActiveActorDestructible powerUp) {
		actorManager.addPowerUp(powerUp, root); // Use ActorManager to add the power-up
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

	protected Group getRoot() {
		return root;
	}

	public Scene getScene() {
		return scene;
	}


	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected double getScreenHeight() {
		return screenHeight;
	}


	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

}
