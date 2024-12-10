package com.example.demo.managers;

import com.example.demo.levels.LevelParent;
import com.example.demo.sounds.SoundManager;
import com.example.demo.screens.AudioControlPanel;
import com.example.demo.screens.MenuView;

import javafx.scene.layout.HBox;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Manages the configuration and display of the game's main scene.
 * <p>
 * This class handles the initialization of the scene, including the background,
 * pause functionality, and audio controls. It also supports displaying error dialogs.
 * </p>
 */
public class SceneManager {

    private final Group root;
    private final Scene scene;
    private final ImageView background;
    private final double screenWidth;
    private final double screenHeight;

    private ImageView pauseButton;
    private final AudioControlPanel audioControlPanel;
    private final SoundManager soundManager;
    private PauseManager pauseManager;
    private final LevelParent levelParent; // Reference to LevelParent
    private final GameLoopManager gameLoopManager;

    public SceneManager(String backgroundImageName, double screenHeight, double screenWidth, SoundManager soundManager,
                        PauseManager pauseManager, LevelParent levelParent) {
        this.root = new Group();
        this.scene = new Scene(root, screenWidth, screenHeight);
        this.background = new ImageView(new Image(MenuView.getResourceOrThrow(backgroundImageName).toExternalForm()));
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.gameLoopManager = GameLoopManager.getInstance();
        this.soundManager = soundManager;
        this.pauseManager = pauseManager;
        this.levelParent = levelParent; // Store LevelParent instance
        this.audioControlPanel = new AudioControlPanel(levelParent);
    }

    /**
     * Initializes the game scene, including the background, and sets it up.
     *
     * @return The initialized Scene object.
     */
    public Scene initializeScene() {
        root.getChildren().clear(); // Clear all children from the root before initializing
        initializeBackground(); // Set up the background and add to root
        addControlPanel();
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
    }

    private void addControlPanel() {
        // Create an HBox to align the pause button and audio control panel horizontally
        HBox controlPanel = new HBox(10); // 10px spacing between elements
        controlPanel.setLayoutX(screenWidth - 200); // Position near the top-right corner
        controlPanel.setLayoutY(20); // Position vertically at the top

        // Ensure the pause button is created
        addPauseButton(null);

        // Add the pause button and audio control panel to the HBox
        controlPanel.getChildren().add(audioControlPanel);
        controlPanel.getChildren().add(pauseButton);

        // Add the control panel to the root
        root.getChildren().add(controlPanel);
    }

    /**
     * Adds the pause button to the root and configures its behavior.
     *
     * @param pauseAction The action to execute when the pause button is clicked.
     */
    public void addPauseButton(Runnable pauseAction) {
        Image pauseImage = new Image(MenuView.getResourceOrThrow("/com/example/demo/images/pause.png").toExternalForm());
        pauseButton = new ImageView(pauseImage);

        pauseButton.setFitWidth(50);
        pauseButton.setFitHeight(50);
        pauseButton.setX(screenWidth - 70);
        pauseButton.setY(20);

        pauseButton.setPickOnBounds(true);
        pauseButton.setFocusTraversable(true);
        pauseButton.setMouseTransparent(false);

        pauseButton.setOnMouseClicked(event -> {
            if (pauseAction != null) {
                pauseAction.run(); // Use the custom pause behavior
            } else {
                if (levelParent == null) {
                    System.err.println("LevelParent is null. Cannot pause the game.");
                    return;
                }

                if (levelParent.isCountdownInProgress()) { // Check countdown state
                    return; // Disable pausing during countdown
                }

                GameLoopManager.getInstance().pause();
                soundManager.pauseBackgroundMusic(); // Pause music
                pauseManager.showPauseScreen(levelParent); // Pass LevelParent reference
            }
        });
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

    /**
     * Retrieves the root {@link Group} of the scene.
     *
     * @return The root {@link Group}.
     */
    public Group getRoot() {
        return root;
    }

    /**
     * Retrieves the {@link Scene} managed by this class.
     *
     * @return The {@link Scene}.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Sets a new {@link PauseManager} for the scene.
     *
     * @param pauseManager The new {@link PauseManager} instance.
     */
    public void setPauseManager(PauseManager pauseManager) {
        this.pauseManager = pauseManager;
    }
}
