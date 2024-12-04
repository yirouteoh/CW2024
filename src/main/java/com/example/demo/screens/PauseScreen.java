package com.example.demo.screens;

import com.example.demo.sounds.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PauseScreen {

    private final Group root; // Root node of the current scene
    private final Runnable resumeAction; // Action to resume the game
    private final Runnable restartAction; // Action to restart the game
    private final Runnable returnToMenuAction; // Action to return to the main menu
    private final SoundManager soundManager; // Sound manager for controlling background music

    // Preloaded images for buttons
    private final Image resumeImage;
    private final Image restartImage;
    private final Image mainMenuImage;

    /**
     * Constructor for PauseScreen.
     *
     * @param root               Root node of the current scene.
     * @param resumeAction       Action to resume the game.
     * @param restartAction      Action to restart the game.
     * @param returnToMenuAction Action to return to the main menu.
     * @param soundManager       Sound manager for controlling background music.
     */
    public PauseScreen(Group root, Runnable resumeAction, Runnable restartAction, Runnable returnToMenuAction, SoundManager soundManager) {
        this.root = root;
        this.resumeAction = resumeAction;
        this.restartAction = restartAction;
        this.returnToMenuAction = returnToMenuAction;
        this.soundManager = soundManager;

        // Preload button images to improve performance
        this.resumeImage = loadImage("/com/example/demo/images/resume.png");
        this.restartImage = loadImage("/com/example/demo/images/restart.png");
        this.mainMenuImage = loadImage("/com/example/demo/images/exit.jpg");
    }

    /**
     * Displays the pause screen overlay.
     */
    public void show() {
        soundManager.pauseBackgroundMusic(); // Pause music

        // Create a semi-transparent overlay
        VBox pauseOverlay = createPauseOverlay();

        // Clear previous overlays to prevent stacking
        root.getChildren().removeIf(node -> node instanceof VBox);

        // Add the overlay to the root node
        root.getChildren().add(pauseOverlay);
    }

    /**
     * Creates the pause overlay.
     *
     * @return A VBox containing the pause overlay.
     */
    private VBox createPauseOverlay() {
        VBox pauseOverlay = new VBox(20); // 20px spacing between buttons
        pauseOverlay.setAlignment(Pos.CENTER); // Center the buttons horizontally and vertically
        pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);"); // Semi-transparent black background

        // Bind the size of the overlay to the scene dimensions
        pauseOverlay.prefWidthProperty().bind(root.getScene().widthProperty());
        pauseOverlay.prefHeightProperty().bind(root.getScene().heightProperty());

        // Create and add buttons
        pauseOverlay.getChildren().addAll(
                createResumeButton(),
                createRestartButton(),
                createMainMenuButton()
        );

        return pauseOverlay;
    }

    /**
     * Creates the resume button.
     *
     * @return A Button to resume the game.
     */
    private Button createResumeButton() {
        Button resumeButton = createButtonWithImage(resumeImage);
        resumeButton.setOnAction(event -> {
            soundManager.resumeBackgroundMusic(); // Resume background music
            resumeAction.run(); // Call the resume action
            removePauseOverlay(); // Remove the pause overlay
        });
        return resumeButton;
    }

    /**
     * Creates the restart button.
     *
     * @return A Button to restart the game.
     */
    private Button createRestartButton() {
        Button restartButton = createButtonWithImage(restartImage);
        restartButton.setOnAction(event -> {
            soundManager.stopBackgroundMusic(); // Stop background music
            restartAction.run(); // Call the restart action
            removePauseOverlay(); // Remove the pause overlay
        });
        return restartButton;
    }

    /**
     * Creates the main menu button.
     *
     * @return A Button to return to the main menu.
     */
    private Button createMainMenuButton() {
        Button mainMenuButton = createButtonWithImage(mainMenuImage);
        mainMenuButton.setOnAction(event -> {
            returnToMenuAction.run(); // Call the return to menu action
            removePauseOverlay(); // Remove the pause overlay
        });
        return mainMenuButton;
    }

    /**
     * Helper method to remove the pause overlay from the root.
     */
    private void removePauseOverlay() {
        root.getChildren().removeIf(node -> node instanceof VBox);
    }

    /**
     * Helper method to create a button with an image.
     *
     * @param image The image to set on the button.
     * @return A Button instance with the specified image.
     */
    private Button createButtonWithImage(Image image) {
        ImageView buttonImageView = new ImageView(image);
        buttonImageView.setFitWidth(70);
        buttonImageView.setFitHeight(70);

        Button button = new Button();
        button.setGraphic(buttonImageView);
        return button;
    }

    /**
     * Helper method to load an image.
     *
     * @param imagePath The path to the image resource.
     * @return The loaded Image instance.
     */
    private Image loadImage(String imagePath) {
        return new Image(getClass().getResource(imagePath).toExternalForm());
    }
}
