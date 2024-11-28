package com.example.demo;

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

    // Constructor for PauseScreen
    public PauseScreen(Group root, Runnable resumeAction, Runnable restartAction, Runnable returnToMenuAction, SoundManager soundManager) {
        this.root = root;
        this.resumeAction = resumeAction;
        this.restartAction = restartAction;
        this.returnToMenuAction = returnToMenuAction;
        this.soundManager = soundManager;
    }

    public void show() {
        soundManager.pauseBackgroundMusic(); // Pause music

        // Create a semi-transparent overlay
        VBox pauseOverlay = new VBox(20); // 20px spacing between buttons
        pauseOverlay.setAlignment(Pos.CENTER); // Center the buttons horizontally and vertically
        pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);"); // Semi-transparent black background

        // Bind the size of the overlay to the scene dimensions
        pauseOverlay.prefWidthProperty().bind(root.getScene().widthProperty());
        pauseOverlay.prefHeightProperty().bind(root.getScene().heightProperty());

        // Resume button
        Button resumeButton = createButtonWithImage("/com/example/demo/images/resume.png");
        resumeButton.setOnAction(event -> {
            soundManager.resumeBackgroundMusic(); // Resume background music
            resumeAction.run(); // Call the resume action (e.g., resume the timeline)
            root.getChildren().remove(pauseOverlay); // Remove the pause overlay
        });

        // Restart button
        Button restartButton = createButtonWithImage("/com/example/demo/images/restart.png");
        restartButton.setOnAction(event -> {
            soundManager.stopBackgroundMusic(); // Stop background music
            restartAction.run(); // Call the restart action
            root.getChildren().remove(pauseOverlay); // Remove the pause overlay
        });

        // Main Menu button
        Button mainMenuButton = createButtonWithImage("/com/example/demo/images/exit.jpg");
        mainMenuButton.setOnAction(event -> {
            returnToMenuAction.run(); // Call the main menu action
            root.getChildren().remove(pauseOverlay); // Remove the pause overlay
        });

        // Add buttons to the overlay
        pauseOverlay.getChildren().addAll(resumeButton, restartButton, mainMenuButton);

        // Clear previous overlays to prevent stacking
        root.getChildren().removeIf(node -> node instanceof VBox);

        // Add the overlay to the root node
        root.getChildren().add(pauseOverlay);
    }


    // Helper method to create a button with an image
    private Button createButtonWithImage(String imagePath) {
        // Load the image
        Image buttonImage = new Image(getClass().getResource(imagePath).toExternalForm());
        ImageView buttonImageView = new ImageView(buttonImage);

        // Set image size
        buttonImageView.setFitWidth(70);
        buttonImageView.setFitHeight(70);

        // Create a button and set the image as its graphic
        Button button = new Button();
        button.setGraphic(buttonImageView);

        return button;
    }
}