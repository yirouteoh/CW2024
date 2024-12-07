package com.example.demo.managers;

import com.example.demo.levels.LevelParent;
import com.example.demo.sounds.SoundManager;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SceneManager {

    private final Group root;
    private final Scene scene;
    private final ImageView background;
    private final double screenWidth;
    private final double screenHeight;

    private ImageView pauseButton;
    private GameLoopManager gameLoopManager;
    private SoundManager soundManager;
    private PauseManager pauseManager;
    private LevelParent levelParent; // Reference to LevelParent

    public SceneManager(String backgroundImageName, double screenHeight, double screenWidth,
                        GameLoopManager gameLoopManager, SoundManager soundManager,
                        PauseManager pauseManager, LevelParent levelParent) {
        this.root = new Group();
        this.scene = new Scene(root, screenWidth, screenHeight);
        this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.gameLoopManager = gameLoopManager;
        this.soundManager = soundManager;
        this.pauseManager = pauseManager;
        this.levelParent = levelParent; // Store LevelParent instance
    }

    /**
     * Initializes the game scene, including the background, and sets it up.
     *
     * @return The initialized Scene object.
     */
    public Scene initializeScene() {
        root.getChildren().clear(); // Clear all children from the root before initializing
        initializeBackground(); // Set up the background and add to root
        return scene; // Return the configured scene
    }

    /**
     * Configures the level background and adds it to the root.
     */
    private void initializeBackground() {
        background.setFocusTraversable(true);
        background.setFitHeight(screenHeight);
        background.setFitWidth(screenWidth);

        root.getChildren().add(background);

        // Add the pause button
        addPauseButton(null);
    }

    /**
     * Adds the pause button to the root and configures its behavior.
     *
     * @param pauseAction The action to execute when the pause button is clicked.
     */
    public void addPauseButton(Runnable pauseAction) {
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
            if (levelParent == null) {
                System.err.println("LevelParent is null. Cannot pause the game.");
                return;
            }

            if (levelParent.isCountdownInProgress()) { // Check countdown state
                return; // Disable pausing during countdown
            }

            gameLoopManager.pause();
            soundManager.pauseBackgroundMusic(); // Pause music
            pauseManager.showPauseScreen(levelParent); // Pass LevelParent reference
        });

        root.getChildren().add(pauseButton);
    }


    /**
     * Displays an error dialog with the specified message.
     *
     * @param message The error message to display.
     */
    public void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public Group getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    public void setPauseManager(PauseManager pauseManager) {
        this.pauseManager = pauseManager;
    }
}
