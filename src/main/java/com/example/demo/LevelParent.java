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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;

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

	private ImageView pauseButton; // Pause button ImageView

	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;

	private int currentNumberOfEnemies;
	private LevelView levelView;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
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
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
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
		} catch (Throwable t) { // Catch Throwable instead of Exception
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

		// Add keyboard controls for game actions
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) {
					user.moveUp();
				} else if (kc == KeyCode.DOWN) {
					user.moveDown();
				} else if (kc == KeyCode.SPACE) {
					fireProjectile();
				}
			}
		});

		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) {
					user.stop();
				}
			}
		});

		root.getChildren().add(background);

		// Add the pause button
		addPauseButton();
	}

	private void addPauseButton() {
		// Load the pause button image
		Image pauseImage = new Image(getClass().getResource("/com/example/demo/images/pause.png").toExternalForm());
		pauseButton = new ImageView(pauseImage);

		// Set button size and position
		pauseButton.setFitWidth(50);
		pauseButton.setFitHeight(50);
		pauseButton.setX(screenWidth - 70); // Position near the top-right corner
		pauseButton.setY(20);

		// Enable interactivity
		pauseButton.setPickOnBounds(true); // Allows clicks on the full image area
		pauseButton.setFocusTraversable(true); // Makes the button focusable
		pauseButton.setMouseTransparent(false); // Ensures it receives mouse events

		// Add click handler for pause button
		pauseButton.setOnMouseClicked(event -> {
			System.out.println("Pause button clicked"); // Debugging statement
			timeline.pause(); // Pause the game
			showPauseScreen(); // Display the pause screen
		});

		// Add pause button to the root group
		root.getChildren().add(pauseButton);
	}


	private void showPauseScreen() {
		PauseScreen pauseScreen = new PauseScreen(
				(Stage) scene.getWindow(),
				() -> timeline.play(), // Resume the game
				() -> System.out.println("Settings action"), // Placeholder for settings
				this::returnToMainMenu // Return to the main menu
		);
		pauseScreen.show(); // Display the pause screen
	}

	private void returnToMainMenu() {
		timeline.stop(); // Stop the game
		Stage stage = (Stage) scene.getWindow(); // Get the current stage
		MenuView menuView = new MenuView(stage, new com.example.demo.controller.Controller(stage));
		menuView.showMenu(); // Transition back to the main menu
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
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
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
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
				if (enemy instanceof Boss) {
					Boss boss = (Boss) enemy;
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

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
			System.out.println("Enemy destroyed! Total kills: " + user.getNumberOfKills());
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
		levelView.showGameOverImage();
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
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
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
