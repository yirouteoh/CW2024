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
    private final Runnable settingsAction; // Action to open settings
    private final Runnable returnToMenuAction; // Action to return to the main menu

    public PauseScreen(Stage gameStage, Runnable resumeAction, Runnable settingsAction, Runnable returnToMenuAction) {
        this.gameStage = gameStage;
        this.resumeAction = resumeAction;
        this.settingsAction = settingsAction;
        this.returnToMenuAction = returnToMenuAction;
    }

    public void show() {
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
                resumeAction.run(); // Resume the game
            }
            pauseStage.close();
        });

        // Settings button with image
        Button settingsButton = createButtonWithImage("/com/example/demo/images/settings.png");
        settingsButton.setOnAction(event -> {
            if (settingsAction != null) {
                settingsAction.run(); // Open settings
            }
        });

        // Return to Main Menu button with image
        Button mainMenuButton = createButtonWithImage("/com/example/demo/images/exit.jpg");
        mainMenuButton.setOnAction(event -> {
            if (returnToMenuAction != null) {
                returnToMenuAction.run(); // Return to menu
            }
            pauseStage.close();
        });

        layout.getChildren().addAll(resumeButton, settingsButton, mainMenuButton);

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
