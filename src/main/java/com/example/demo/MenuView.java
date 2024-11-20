package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;


public class MenuView {

    private final Stage stage;
    private final com.example.demo.controller.Controller controller;

    // Constructor to accept Stage and Controller
    public MenuView(Stage stage, com.example.demo.controller.Controller controller) {
        this.stage = stage;
        this.controller = controller;
    }

    // Show the menu
    public void showMenu() {
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
        Button exitButton = createStyledButton("Exit");

        // Button actions
        playButton.setOnAction(e -> {
            try {
                controller.launchGame(); // Launch the game using the controller
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        exitButton.setOnAction(e -> System.exit(0));

        // Panel layout
        VBox panel = new VBox(20, title, playButton, exitButton);
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
}