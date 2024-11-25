package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;

	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;
	private final KillCountDisplay killCountDisplay;

	private ImageView pauseButton; // Pause button ImageView

	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	private final List<ActiveActorDestructible> powerUps;


	private int currentNumberOfEnemies;
	private LevelView levelView;

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

		initializeTimeline();
		friendlyUnits.add(user);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground(); // Set up the background and add to root
		initializeFriendlyUnits(); // Add the user plane and other units
		levelView.showHeartDisplay(); // Show health or level-related UI

		// Add the kill count display to the root
		root.getChildren().add(killCountDisplay.getDisplay());

		// Add key press and release handlers to the scene
		scene.setOnKeyPressed(event -> {
			switch (event.getCode()) {
				case UP -> user.moveUp();
				case DOWN -> user.moveDown();
				case LEFT -> user.moveLeft();
				case RIGHT -> user.moveRight();
				case SPACE -> fireProjectile(); // Fire projectile when space is pressed
			}
		});

		scene.setOnKeyReleased(event -> {
			switch (event.getCode()) {
				case UP, DOWN -> user.stop(); // Stop vertical movement
				case LEFT, RIGHT -> user.stopHorizontal(); // Stop horizontal movement
			}
		});

		return scene; // Return the configured scene
	}

	protected KillCountDisplay getKillCountDisplay() {
		return killCountDisplay;
	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	public void goToNextLevel(String levelName) {
		try {
			timeline.stop(); // Stop current level's timeline
			Class<?> nextLevelClass = Class.forName(levelName);
			LevelParent nextLevel = (LevelParent) nextLevelClass
					.getConstructor(double.class, double.class)
					.newInstance(screenHeight, screenWidth);

			Scene nextScene = nextLevel.initializeScene();
			Stage primaryStage = (Stage) root.getScene().getWindow();
			primaryStage.setScene(nextScene);
			nextLevel.startGame();
		} catch (Throwable t) {
			showErrorDialog("Error transitioning to next level: " + t.getMessage());
		}
	}

	private void showErrorDialog(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		handlePowerUpCollisions(); // Handle power-up collection
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);

		root.getChildren().add(background);

		// Add the pause button
		addPauseButton();
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
			showPauseScreen();
		});

		root.getChildren().add(pauseButton);
	}

	private void showPauseScreen() {
		PauseScreen pauseScreen = new PauseScreen(
				(Stage) scene.getWindow(),
				() -> timeline.play(),
				() -> System.out.println("Settings action"),
				this::returnToMainMenu
		);
		pauseScreen.show();
	}

	private void returnToMainMenu() {
		timeline.stop();
		Stage stage = (Stage) scene.getWindow();
		MenuView menuView = new MenuView(stage, new com.example.demo.controller.Controller(stage));
		menuView.showMenu();
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


	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
		powerUps.forEach(powerUp -> powerUp.updateActor());
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

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
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

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
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

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		int kills = currentNumberOfEnemies - enemyUnits.size();
		for (int i = 0; i < kills; i++) {
			killCountDisplay.incrementKillCount();
			System.out.println("Enemy destroyed! Total kills: " + killCountDisplay.getKillCount());
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		GameOverImage gameOverImage = new GameOverImage(screenWidth, screenHeight);
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

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		if (!enemyUnits.contains(enemy) && !getRoot().getChildren().contains(enemy)) {
			enemyUnits.add(enemy);
			getRoot().getChildren().add(enemy);
		}
	}


	protected void addPowerUp(PowerUp powerUp) {
		if (!powerUps.contains(powerUp) && !getRoot().getChildren().contains(powerUp)) {
			powerUps.add(powerUp);
			getRoot().getChildren().add(powerUp);
		}
	}


	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
}
