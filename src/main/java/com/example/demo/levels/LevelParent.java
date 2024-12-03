package com.example.demo.levels;

import java.util.*;
import java.util.stream.Collectors;
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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class LevelParent {

	// Constants
	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;

	// Core Properties
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;
	private final KillCountDisplay killCountDisplay;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	private final List<ActiveActorDestructible> powerUps;

	private ImageView pauseButton; // Pause button ImageView
	private SoundManager soundManager;
	private boolean isGameOver = false;
	private boolean isGameWon = false;

	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	private int currentNumberOfEnemies;
	private LevelView levelView;
	private CountdownOverlay countdownOverlay;
	private boolean countdownInProgress = false; // Flag to track countdown state

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth, int targetKillCount) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.user.setLevelParent(this);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.powerUps = new ArrayList<>();

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

		initializeTimeline();
		friendlyUnits.add(user);
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	protected abstract void initializeFriendlyUnits();
	protected abstract void checkIfGameOver();
	protected abstract void spawnEnemyUnits();
	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground(); // Set up the background and add to root
		initializeFriendlyUnits(); // Add the user plane and other units
		levelView.showHeartDisplay(); // Show health or level-related UI
		root.getChildren().add(killCountDisplay.getDisplay()); // Add the kill count display to the root
		initializeEventHandlers();
		return scene; // Return the configured scene
	}

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
					if (!isGameOver && !isGameWon && !timeline.getStatus().equals(Timeline.Status.PAUSED)) {
						timeline.pause();
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
			timeline.play(); // Start the timeline after countdown ends
		});
	}



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

	private void updateActors() {
		updateActorList(friendlyUnits);
		updateActorList(enemyUnits);
		updateActorList(userProjectiles);
		updateActorList(enemyProjectiles);
		updateActorList(powerUps);
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
		removeAllDestroyedActors();
	}

	/**
	 * Updates UI elements like kill counts and level view.
	 */
	private void updateUIElements() {
		updateKillCount();
		updateLevelView();
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	private void updateActorList(List<ActiveActorDestructible> actors) {
		actors.forEach(ActiveActorDestructible::updateActor);
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
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
		for (ActiveActorDestructible projectile : enemyProjectiles) {
			if (projectile.getBoundsInParent().intersects(user.getBoundsInParent())) {
				user.takeDamage();
				projectile.destroy();
				soundManager.playCrashSound(); // Play crash sound
				shakeScreen(); // Trigger screen shake
			}
		}
	}

	private void handlePlaneCollisions() {
		for (ActiveActorDestructible friendly : friendlyUnits) {
			for (ActiveActorDestructible enemy : enemyUnits) {
				if (friendly instanceof UserPlane && friendly.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
					friendly.takeDamage();
					enemy.takeDamage();
					soundManager.playCrashSound(); // Play crash sound
					shakeScreen(); // Trigger screen shake
				}
			}
		}
	}

	private void handlePowerUpCollisions() {
		for (ActiveActorDestructible powerUp : powerUps) {
			if (powerUp.getBoundsInParent().intersects(user.getBoundsInParent())) {
				if (powerUp instanceof SpreadshotPowerUp) {
					((SpreadshotPowerUp) powerUp).activate(user); // Activate spreadshot
				} else if (powerUp instanceof PowerUp) {
					((PowerUp) powerUp).activate(user); // Activate default power-ups
				}

				powerUp.destroy(); // Remove power-up after collection
			}
		}
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
		removeDestroyedActors(powerUps);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(ActiveActorDestructible::isDestroyed)
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void updateKillCount() {
		int kills = currentNumberOfEnemies - enemyUnits.size();
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
			timeline.stop(); // Stop the current level's timeline
			Scene nextScene = nextLevel.initializeScene();
			Stage primaryStage = (Stage) root.getScene().getWindow();
			primaryStage.setScene(nextScene);
			nextLevel.startGame(); // Start the next level
		} catch (Exception e) {
			showErrorDialog("Error transitioning to next level: " + e.getMessage());
		}
	}

	private void showErrorDialog(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		if (projectile != null && !root.getChildren().contains(projectile)) {
			root.getChildren().add(projectile);
			userProjectiles.add(projectile);
		}
	}

	public void addProjectile(ActiveActorDestructible projectile) {
		if (!userProjectiles.contains(projectile) && !getRoot().getChildren().contains(projectile)) {
			getRoot().getChildren().add(projectile); // Add to the scene graph
			userProjectiles.add(projectile);         // Track the projectile
		}
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
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
			timeline.pause();
			soundManager.pauseBackgroundMusic(); // Pause music
			showPauseScreen();
		});

		root.getChildren().add(pauseButton);
	}

	private void showPauseScreen() {
		timeline.pause(); // Pause the game timeline
		soundManager.pauseBackgroundMusic(); // Pause the music

		PauseScreen pauseScreen = new PauseScreen(
				root, // Pass the root Group
				() -> {
					timeline.play(); // Resume the game timeline
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
			timeline.stop(); // Stop the current game timeline
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
		timeline.stop(); // Stop the game
		Stage stage = (Stage) scene.getWindow(); // Get the current stage

		// Assuming MenuView handles showing the main menu
		MenuView menuView = new MenuView(stage, new com.example.demo.controller.Controller(stage));
		menuView.showMenu(); // Display the main menu
	}


	protected void winGame() {
		timeline.stop();
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
		timeline.stop();
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

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	private void addActor(ActiveActorDestructible actor, List<ActiveActorDestructible> actorList) {
		if (!actorList.contains(actor) && !root.getChildren().contains(actor)) {
			actorList.add(actor);
			root.getChildren().add(actor);
		}
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		addActor(enemy, enemyUnits);
	}

	protected void addPowerUp(PowerUp powerUp) {
		addActor(powerUp, powerUps);
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
