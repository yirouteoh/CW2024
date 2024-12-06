package com.example.demo.levels;

import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.example.demo.actors.ActiveActorDestructible;
import com.example.demo.actors.plane.Boss;
import com.example.demo.actors.plane.FighterPlane;
import com.example.demo.actors.plane.UserPlane;
import com.example.demo.powerups.PowerUp;
import com.example.demo.powerups.SpreadshotPowerUp;
import com.example.demo.screens.*;
import com.example.demo.sounds.SoundManager;
import com.example.demo.utils.KillCountDisplay;
import com.example.demo.managers.ActorManager;
import com.example.demo.managers.GameLoopManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

	private final Group root;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;
	private final KillCountDisplay killCountDisplay;

	private final ActorManager actorManager = new ActorManager();
	private GameLoopManager gameLoopManager;

	private ImageView pauseButton; // Pause button ImageView
	private SoundManager soundManager;
	private boolean isGameOver = false;
	private boolean isGameWon = false;

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

		initializeGameLoop();
		actorManager.addFriendlyUnit(user, root);
	}

	/**
	 * Initializes the timeline that controls the game loop.
	 * The timeline runs indefinitely and invokes the `updateScene` method at fixed intervals.
	 */
	private void initializeGameLoop() {
		gameLoopManager = new GameLoopManager(Duration.millis(MILLISECOND_DELAY), this::updateScene);
	}

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
		initializeEventHandlers();
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

	/**
	 * Adds event handlers for user inputs such as movement, firing, and pausing.
	 */
	private void initializeEventHandlers() {
		scene.setOnKeyPressed(event -> {
			if (countdownInProgress) return; // Ignore input during countdown

			switch (event.getCode()) {
				case UP -> user.moveUp();
				case DOWN -> user.moveDown();
				case LEFT -> user.moveLeft();
				case RIGHT -> user.moveRight();
				case SPACE -> {
					fireProjectile(); // Fire projectile
					soundManager.playShootSound(); // Play shooting sound effect
				}
				case ESCAPE -> {
					if (!isGameOver && !isGameWon && !gameLoopManager.isPaused()) {
						gameLoopManager.pause();
						soundManager.pauseBackgroundMusic();
						showPauseScreen();
					}
				}
			}
		});

		scene.setOnKeyReleased(event -> {
			if (countdownInProgress) return; // Ignore input during countdown

			switch (event.getCode()) {
				case UP, DOWN -> user.stop(); // Stop vertical movement
				case LEFT, RIGHT -> user.stopHorizontal(); // Stop horizontal movement
			}
		});
	}

	public void startGame() {
		countdownInProgress = true; // Disable input during countdown
		background.requestFocus(); // Ensure the background receives focus for key events

		// Use CountdownOverlay to show a 3-2-1-GO sequence
		CountdownOverlay countdownOverlay = new CountdownOverlay(root, screenWidth, screenHeight);
		countdownOverlay.showCountdown(() -> {
			countdownInProgress = false; // Re-enable input after countdown
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
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		handlePowerUpCollisions();
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

	// Methods responsible for detecting and handling collisions between actors, projectiles, and power-ups.

	/**
	 * Processes all collision-related logic in the game, including projectile, plane, and power-up interactions.
	 */

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : actorManager.getEnemyUnits()) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(actorManager.getUserProjectiles(), actorManager.getEnemyUnits());
	}

	private void handleCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> enemies) {
		for (ActiveActorDestructible projectile : projectiles) {
			for (ActiveActorDestructible enemy : enemies) {
				if (enemy instanceof Boss boss) {
					if (boss.getCustomHitbox().intersects(projectile.getBoundsInParent())) {
						if (!boss.isShielded()) {
							projectile.takeDamage();
							boss.takeDamage();
						}
					}
				} else if (enemy.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
					projectile.takeDamage();
					enemy.takeDamage();
				}
			}
		}
	}


	private void handleEnemyProjectileCollisions() {
		actorManager.getEnemyProjectiles().forEach(projectile -> {
			if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
				user.takeDamage();
				projectile.destroy();
				soundManager.playCrashSound(); // Play crash sound
				shakeScreen(); // Trigger screen shake
			}
		});
	}


	private void handlePlaneCollisions() {
		actorManager.getFriendlyUnits().forEach(friendly -> {
			actorManager.getEnemyUnits().forEach(enemy -> {
				if (friendly instanceof UserPlane && friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
					friendly.takeDamage();
					enemy.takeDamage();
					soundManager.playCrashSound(); // Play crash sound
					shakeScreen(); // Trigger screen shake
				}
			});
		});
	}


	private void handlePowerUpCollisions() {
		actorManager.getPowerUps().forEach(powerUp -> {
			if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
				if (powerUp instanceof SpreadshotPowerUp spreadshot) {
					spreadshot.activate(user); // Activate spreadshot
				} else if (powerUp instanceof PowerUp defaultPowerUp) {
					defaultPowerUp.activate(user); // Activate default power-ups
				}
				powerUp.destroy(); // Remove power-up after collection
			}
		});
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
			Stage primaryStage = (Stage) root.getScene().getWindow();
			primaryStage.setScene(nextScene);
			nextLevel.startGame(); // Start the next level
		} catch (Exception e) {
			showErrorDialog("Error transitioning to next level: " + e.getMessage());
		}
	}


	private void showErrorDialog(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.show(); // Use non-blocking show() instead of showAndWait()
	}


	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null) {
			actorManager.addUserProjectile(projectile, root);
		}
	}

	public void addProjectile(ActiveActorDestructible projectile) {
		actorManager.addUserProjectile(projectile, root); // Use ActorManager to add the projectile
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			actorManager.addEnemyProjectile(projectile, root);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}


	private void shakeScreen() {
		final double amplitude = 10; // How far the screen moves
		final int cycles = 5; // Number of back-and-forth movements
		Timeline timeline = new Timeline();
		for (int i = 0; i < cycles; i++) {
			double offset = (i % 2 == 0) ? amplitude : -amplitude; // Alternate directions
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50 * i), e -> root.setTranslateX(offset)));
		}
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50 * cycles), e -> root.setTranslateX(0))); // Reset position
		timeline.play();
	}


	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
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

	private void showPauseScreen() {
		gameLoopManager.pause(); // Pause the game timeline
		soundManager.pauseBackgroundMusic(); // Pause the music

		PauseScreen pauseScreen = new PauseScreen(
				root, // Pass the root Group
				() -> {
					gameLoopManager.resume();// Resume the game timeline
					soundManager.resumeBackgroundMusic(); // Resume the music
				},
				this::restartGame, // Restart the game
				this::returnToMainMenu, // Return to the main menu
				soundManager // Pass the SoundManager instance
		);
		pauseScreen.show(); // Display the pause screen
	}

	private void restartGame() {
		try {
			isGameWon = false; // Reset the game won flag
			isGameOver = false; // Reset game over flag
			gameLoopManager.stop();// Stop the current game timeline
			soundManager.stopBackgroundMusic(); // Stop the music

			// Assuming Controller handles restarting
			com.example.demo.controller.Controller controller = new com.example.demo.controller.Controller((Stage) scene.getWindow());
			controller.launchGame(); // Restart the game from Level One
		} catch (Exception e) {
			showErrorDialog("Error restarting the game: " + e.getMessage());
		}
	}

	private void returnToMainMenu() {
		isGameWon = false; // Reset the game won flag
		isGameOver = false; // Reset game over flag
		gameLoopManager.stop(); // Stop the game
		Stage stage = (Stage) scene.getWindow(); // Get the current stage

		// Assuming MenuView handles showing the main menu
		MenuView menuView = new MenuView(stage, new com.example.demo.controller.Controller(stage));
		menuView.showMenu(); // Display the main menu
	}


	protected void winGame() {
		gameLoopManager.stop();
		soundManager.stopBackgroundMusic();
		soundManager.playBackgroundMusic(SoundManager.WIN_GAME_MUSIC); // Play the win music
		isGameWon = true; // Set the game won flag

		// Add the enhanced WinImage to the root
		WinImage winScreen = new WinImage(
				screenWidth,
				screenHeight,
				this::restartGame,       // Restart callback
				this::returnToMainMenu   // Exit callback
		);
		root.getChildren().add(winScreen);
	}


	protected void loseGame() {
		gameLoopManager.stop();
		soundManager.stopBackgroundMusic();
		soundManager.playBackgroundMusic(SoundManager.GAME_OVER_MUSIC); // Play the Game Over music
		isGameOver = true; // Set game over flag

		// Add the GameOverImage to the root
		GameOverImage gameOverImage = new GameOverImage(
				screenWidth,
				screenHeight,
				this::restartGame,       // Pass the restart callback
				this::returnToMainMenu   // Pass the exit callback
		);
		root.getChildren().add(gameOverImage);
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

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
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
