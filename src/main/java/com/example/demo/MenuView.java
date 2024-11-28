package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class MenuView {

    private final Stage stage;
    private final com.example.demo.controller.Controller controller;
    private final SoundManager soundManager;

    private boolean isMuted = false; // Track mute state

    // Constructor to accept Stage and Controller
    public MenuView(Stage stage, com.example.demo.controller.Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.soundManager = SoundManager.getInstance();
    }

    // Show the menu
    public void showMenu() {
        // Play the menu background music
        soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);

        // Root layout
        StackPane root = new StackPane();

        // Load the background image
        Image backgroundImage = new Image(getClass().getResource("/com/example/demo/images/background1.jpeg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
        );
        root.setBackground(new Background(background)); // Set the image as the background

        // Create menu layout
        VBox menuPanel = createMenuPanel();

        // Add the menu panel on top of the background
        root.getChildren().add(menuPanel);

        // Scene setup
        Scene menuScene = new Scene(root, 800, 600);
        menuScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // Link CSS file
        stage.setScene(menuScene);
        stage.setTitle("Sky Battle Menu");
        stage.show();
    }

    private VBox createMenuPanel() {
        // Title bar with green background and rounded corners
        Rectangle titleBackground = new Rectangle(700, 180, Color.GREEN);
        titleBackground.setArcWidth(20);
        titleBackground.setArcHeight(20);

        Text titleText = new Text("SKY BATTLE");
        titleText.getStyleClass().add("banner-text");

        StackPane title = new StackPane(titleBackground, titleText);

        // Buttons
        Button playButton = createStyledButton("Play");
        Button instructionsButton = createStyledButton("Instructions");
        Button muteButton = createStyledButton("Mute");
        Button exitButton = createStyledButton("Exit");

        // Button actions
        playButton.setOnAction(e -> {
            try {
                soundManager.stopBackgroundMusic(); // Stop the menu background music
                controller.launchGame(); // Launch the game using the controller
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        instructionsButton.setOnAction(e -> showInstructions());

        muteButton.setOnAction(e -> toggleMute(muteButton));

        exitButton.setOnAction(e -> System.exit(0));

        // Panel layout
        VBox panel = new VBox(20, title, playButton, instructionsButton, muteButton, exitButton);
        panel.setAlignment(Pos.CENTER);
        panel.getStyleClass().add("menu-panel"); // Apply CSS styling
        panel.setPadding(new javafx.geometry.Insets(20));

        return panel;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button"); // Apply button styling from CSS
        return button;
    }

    private void showInstructions() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Instructions");
        alert.setHeaderText("How to Play Sky Battle");
        alert.setContentText("1. Use arrow keys to navigate your plane.\n"
                + "2. Press space to shoot.\n"
                + "3. Destroy enemy planes and avoid obstacles.\n"
                + "4. Survive and progress through levels to win!");
        alert.showAndWait();
    }

    private void toggleMute(Button muteButton) {
        isMuted = !isMuted; // Toggle the mute state
        if (isMuted) {
            soundManager.stopBackgroundMusic();
            muteButton.setText("Unmute");
        } else {
            soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);
            muteButton.setText("Mute");
        }
    }
}