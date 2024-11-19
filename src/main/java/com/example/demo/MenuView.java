package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
        // Root layout with a gradient background
        StackPane root = new StackPane();
        root.getStyleClass().add("menu-root"); // Apply root background styling from CSS

        // Create menu layout
        VBox menuPanel = createMenuPanel();

        // Add the menu panel to the root
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
        Rectangle titleBackground = new Rectangle(250, 50, Color.GREEN);
        titleBackground.setArcWidth(20);
        titleBackground.setArcHeight(20);

        Text titleText = new Text("MENU");
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
