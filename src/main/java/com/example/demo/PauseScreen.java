package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PauseScreen {

    private final Stage gameStage; // Main game stage
    private final Runnable resumeAction; // Action to resume the game
    private final Runnable restartAction; // Action to restart the game
    private final Runnable returnToMenuAction; // Action to return to the main menu
    private final SoundManager soundManager;


    public PauseScreen(Stage gameStage, Runnable resumeAction, Runnable restartAction, Runnable returnToMenuAction, SoundManager soundManager) {
        this.gameStage = gameStage;
        this.resumeAction = resumeAction;
        this.restartAction = restartAction; // Add restart logic
        this.returnToMenuAction = returnToMenuAction;
        this.soundManager = soundManager; // Initialize SoundManager
    }


    public void show() {
        soundManager.pauseBackgroundMusic(); // Pause music when the pause screen is shown
        // Pause overlay stage
        Stage pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL); // Prevent interactions with the main stage
        pauseStage.setTitle("Game Paused");

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        // Resume button with image
        Button resumeButton = createButtonWithImage("/com/example/demo/images/resume.png");
        resumeButton.setOnAction(event -> {
            if (resumeAction != null) {
                soundManager.resumeBackgroundMusic(); // Resume music
                resumeAction.run(); // Resume the game
            }
            pauseStage.close();
        });

        // Restart button with image
        Button restartButton = createButtonWithImage("/com/example/demo/images/restart.png");
        restartButton.setOnAction(event -> {
            if (restartAction != null) {
                soundManager.stopBackgroundMusic(); // Stop current music
                restartAction.run(); // Restart the game
            }
            pauseStage.close();
        });

        // Return to Main Menu button with image
        Button mainMenuButton = createButtonWithImage("/com/example/demo/images/exit.jpg");
        mainMenuButton.setOnAction(event -> {
            if (returnToMenuAction != null) {
                returnToMenuAction.run(); // Return to menu
            }
            pauseStage.close();
        });

        layout.getChildren().addAll(resumeButton, restartButton, mainMenuButton);

        Scene pauseScene = new Scene(layout, 400, 300);
        pauseStage.setScene(pauseScene);
        pauseStage.showAndWait(); // Block interaction until the pause stage is closed
    }

    private Button createButtonWithImage(String imagePath) {
        Image buttonImage = new Image(getClass().getResource(imagePath).toExternalForm());
        ImageView buttonImageView = new ImageView(buttonImage);
        buttonImageView.setFitWidth(50);
        buttonImageView.setFitHeight(50);

        Button button = new Button();
        button.setGraphic(buttonImageView);
        return button;
    }
}
