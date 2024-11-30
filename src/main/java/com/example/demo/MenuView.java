package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

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

        // Load the background image and set it to cover the screen
        Image backgroundImage = new Image(getResourceOrThrow("/com/example/demo/images/background1.jpeg").toExternalForm());
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(
                        BackgroundSize.AUTO,  // Use auto width
                        BackgroundSize.AUTO,  // Use auto height
                        false,                // Width not percentage
                        false,                // Height not percentage
                        false,                // Do not contain
                        true                  // Cover the entire scene
                )
        );
        root.setBackground(new Background(background)); // Set the image as the background

        // Create menu layout
        VBox menuPanel = createMenuPanel();

        // Add the menu panel on top of the background
        root.getChildren().add(menuPanel);

        // Scene setup
        Scene menuScene = new Scene(root, 800, 600); // Adjust resolution as needed
        menuScene.getStylesheets().add(getResourceOrThrow("/style.css").toExternalForm()); // Link CSS file
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

        instructionsButton.setOnAction(e -> showCustomInstructions());

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

    private void showCustomInstructions() {
        // Create a transparent overlay to dim the background
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Semi-transparent black background
        overlay.setAlignment(Pos.CENTER);

        // Create the container for the instructions dialog
        VBox container = new VBox(20);
        container.setStyle("-fx-background-color: #dff9fb; -fx-padding: 20; -fx-border-color: #badc58; -fx-border-width: 3; -fx-background-radius: 10;");
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(500);
        container.setMaxHeight(400);

        // Create the content for the first page (How to Play)
        VBox page1 = new VBox(15);
        page1.setAlignment(Pos.CENTER);
        ImageView icon1 = new ImageView(new Image(getResourceOrThrow("/com/example/demo/images/instructions.png").toExternalForm()));
        icon1.setFitHeight(100);
        icon1.setFitWidth(100);
        Text howToPlayText = new Text("How to Play Sky Battle\n\n"
                + "1. Use arrow keys to navigate your plane.\n"
                + "2. Press space to shoot.\n"
                + "3. Destroy enemy planes and avoid obstacles.\n"
                + "4. Collect power up at level 3 to spreadshot!\n"
                + "5. Survive and progress through levels to win!");

        howToPlayText.setStyle("-fx-font-size: 16px; -fx-text-fill: #30336b;");
        howToPlayText.setWrappingWidth(450);
        page1.getChildren().addAll(icon1, howToPlayText);

        // Create the content for the second page (Settings Control)
        VBox page2 = new VBox(15);
        page2.setAlignment(Pos.CENTER);
        ImageView icon2 = new ImageView(new Image(getResourceOrThrow("/com/example/demo/images/settings.png").toExternalForm()));
        icon2.setFitHeight(100);
        icon2.setFitWidth(100);
        Text settingsControlText = new Text("Control Key\n\n"
                + "1. Arrow Keys: Navigate your plane.\n"
                + "2. Spacebar: Shoot bullets.\n"
                + "3. ESC: Pause the game.");
        settingsControlText.setStyle("-fx-font-size: 16px; -fx-text-fill: #30336b;");
        settingsControlText.setWrappingWidth(450);
        page2.getChildren().addAll(icon2, settingsControlText);

        // Use a StackPane to switch between pages
        StackPane pages = new StackPane(page1, page2);
        page2.setVisible(false); // Start with the first page visible

        // Navigation buttons
        // For the first page, only show "Next" and "Close"
        HBox firstPageButtons = new HBox(10);
        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #74b9ff; -fx-text-fill: white; -fx-font-size: 14px;");
        nextButton.setOnAction(e -> {
            page1.setVisible(false);
            page2.setVisible(true);
        });
        Button closeButton1 = new Button("Close");
        closeButton1.setStyle("-fx-background-color: #ffbe76; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton1.setOnAction(e -> {
            overlay.setVisible(false);
            StackPane root = (StackPane) stage.getScene().getRoot();
            root.getChildren().remove(overlay);
        });
        firstPageButtons.getChildren().addAll(nextButton, closeButton1);
        firstPageButtons.setAlignment(Pos.CENTER);

        // For the second page, only show "Previous" and "Close"
        HBox secondPageButtons = new HBox(10);
        Button prevButton = new Button("Previous");
        prevButton.setStyle("-fx-background-color: #74b9ff; -fx-text-fill: white; -fx-font-size: 14px;");
        prevButton.setOnAction(e -> {
            page1.setVisible(true);
            page2.setVisible(false);
        });
        Button closeButton2 = new Button("Close");
        closeButton2.setStyle("-fx-background-color: #ffbe76; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton2.setOnAction(e -> {
            overlay.setVisible(false);
            StackPane root = (StackPane) stage.getScene().getRoot();
            root.getChildren().remove(overlay);
        });
        secondPageButtons.getChildren().addAll(prevButton, closeButton2);
        secondPageButtons.setAlignment(Pos.CENTER);

        // Add appropriate buttons to each page
        page1.getChildren().add(firstPageButtons);
        page2.getChildren().add(secondPageButtons);

        // Add the pages to the overlay
        container.getChildren().addAll(pages);
        overlay.getChildren().add(container);

        // Add the overlay to the root pane (the main StackPane of your menu)
        StackPane root = (StackPane) stage.getScene().getRoot();
        root.getChildren().add(overlay);

        // Make the overlay visible
        overlay.setVisible(true);
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

    private URL getResourceOrThrow(String resourcePath) {
        URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return resourceUrl;
    }
}
