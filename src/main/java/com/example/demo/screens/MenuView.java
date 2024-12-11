package com.example.demo.screens;

import com.example.demo.sounds.SoundManager;


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

import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import java.util.logging.Logger;


import java.net.URL;

public class MenuView {

    private final Stage stage;
    private final com.example.demo.controller.Controller controller;
    private final SoundManager soundManager;
    private final Image instructionsImage;
    private final Image settingsImage;
    private final Image unmuteMusicImage;
    private final Image unmuteSoundImage;
    private static final Logger logger = Logger.getLogger(MenuView.class.getName());


    // Constructor
    public MenuView(Stage stage, com.example.demo.controller.Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.soundManager = SoundManager.getInstance();

        // Preload images
        this.instructionsImage = new Image(getResourceOrThrow("/com/example/demo/images/instructions.png").toExternalForm());
        this.settingsImage = new Image(getResourceOrThrow("/com/example/demo/images/settings.png").toExternalForm());
        // In the constructor
        this.unmuteMusicImage = new Image(getResourceOrThrow("/com/example/demo/images/unmutemusic.png").toExternalForm());
        this.unmuteSoundImage = new Image(getResourceOrThrow("/com/example/demo/images/unmuteeffect.png").toExternalForm());
    }

    /**
     * Displays the main menu with background music and layout.
     */
    public void showMenu() {
        soundManager.playBackgroundMusic(SoundManager.MENU_MUSIC);

        // Create root pane and menu panel
        StackPane root = createRootPane();
        VBox menuPanel = createMenuPanel();

        // Create the exit button
        Button exitButton = createExitButton();

        // Align and add the exit button to the root
        StackPane.setAlignment(exitButton, Pos.TOP_LEFT);
        StackPane.setMargin(exitButton, new javafx.geometry.Insets(10)); // Margin from top-left corner

        // Add menu panel and exit button to the root
        root.getChildren().addAll(menuPanel, exitButton);

        // Set up the scene
        Scene menuScene = new Scene(root, 800, 600);
        menuScene.getStylesheets().add(getResourceOrThrow("/style.css").toExternalForm());

        stage.setScene(menuScene);
        stage.setTitle("Sky Battle Menu");
        stage.show();
    }

    /**
     * Creates an exit button and attaches a close action.
     *
     * @return Button configured as the exit button.
     */
    private Button createExitButton() {
        // Load the exit icon
        ImageView exitIcon = new ImageView(new Image(getResourceOrThrow("/com/example/demo/images/cross.png").toExternalForm()));
        exitIcon.setFitHeight(60);
        exitIcon.setFitWidth(60);

        // Create the button and set the icon
        Button exitButton = new Button();
        exitButton.setGraphic(exitIcon);
        exitButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        // Add smooth scaling effects with animations
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), exitButton);
        scaleUp.setToX(1.2); // Scale to 120%
        scaleUp.setToY(1.2);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), exitButton);
        scaleDown.setToX(1.0); // Scale back to normal size
        scaleDown.setToY(1.0);

        exitButton.setOnMouseEntered(e -> scaleUp.playFromStart());
        exitButton.setOnMouseExited(e -> scaleDown.playFromStart());

        // Set button action
        exitButton.setOnAction(e -> {
            soundManager.stopBackgroundMusic();
            stage.close();
        });

        return exitButton;
    }

    /**
     * Creates the root pane with a background image.
     *
     * @return StackPane with background.
     */
    private StackPane createRootPane() {
        StackPane root = new StackPane();
        Image backgroundImage = new Image(getResourceOrThrow("/com/example/demo/images/background1.jpeg").toExternalForm());

        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true)
        );

        root.setBackground(new Background(background));
        return root;
    }

    /**
     * Creates the menu panel with title and buttons.
     *
     * @return VBox containing menu elements.
     */
    private VBox createMenuPanel() {
        StackPane title = createTitle();

        Button playButton = createStyledButton("Play", e -> launchGame());
        Button instructionsButton = createStyledButton("Instructions", e -> {
            InstructionsPage instructionsPage = new InstructionsPage(stage, instructionsImage, settingsImage);
            instructionsPage.show();
        });
        Button audioSettingsButton = createStyledButton("Audio Settings", e -> {
            AudioSettingsPage audioSettingsPage = new AudioSettingsPage(stage, soundManager, unmuteMusicImage, unmuteSoundImage);
            audioSettingsPage.show();
        });
        Button exitButton = createStyledButton("Exit", e -> System.exit(0));

        VBox panel = new VBox(20, title, playButton, instructionsButton, audioSettingsButton, exitButton);
        panel.setAlignment(Pos.CENTER);
        panel.getStyleClass().add("menu-panel");
        panel.setPadding(new javafx.geometry.Insets(20));

        return panel;
    }

    /**
     * Creates the title bar for the menu.
     *
     * @return StackPane containing the title.
     */
    private StackPane createTitle() {
        Rectangle titleBackground = new Rectangle(700, 180, Color.GREEN);
        titleBackground.setArcWidth(20);
        titleBackground.setArcHeight(20);

        Text titleText = new Text("SKY BATTLE");
        titleText.getStyleClass().add("banner-text");

        return new StackPane(titleBackground, titleText);
    }

    /**
     * Creates a styled button with a given text and action.
     *
     * @param text Button text.
     * @param action Event handler for the button.
     * @return Button instance with hover effects.
     */
    private Button createStyledButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        button.setOnAction(action);

        // Create ScaleTransitions for hover effect
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), button);
        scaleUp.setToX(1.2); // Scale to 120%
        scaleUp.setToY(1.2);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), button);
        scaleDown.setToX(1.0); // Scale back to original size
        scaleDown.setToY(1.0);

        // Add hover event handlers
        button.setOnMouseEntered(e -> {
            scaleDown.stop(); // Stop any ongoing scaleDown animation
            scaleUp.playFromStart(); // Play scaleUp animation immediately
        });

        button.setOnMouseExited(e -> {
            scaleUp.stop(); // Stop any ongoing scaleUp animation
            scaleDown.playFromStart(); // Play scaleDown animation immediately
        });

        return button;
    }

    /**
     * Launches the game by notifying the controller.
     */
    private void launchGame() {
        try {
            soundManager.stopBackgroundMusic();
            controller.launchGame();
        } catch (Exception ex) {
            logger.severe("Error launching game: " + ex.getMessage());
        }
    }

    /**
     * Retrieves a resource or throws an exception if not found.
     *
     * @param resourcePath The resource path.
     * @return The resource URL.
     */
    public static URL getResourceOrThrow(String resourcePath) {
        URL resourceUrl = MenuView.class.getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        return resourceUrl;
    }
}
